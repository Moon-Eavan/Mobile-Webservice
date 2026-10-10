import tempfile

from django.contrib.auth import get_user_model
from django.test import override_settings
from rest_framework import status
from rest_framework.test import APITestCase

from .models import Post

# Django test runner는 settings.DATABASES와 별개로 전용 테스트 DB를 새로 만들고
# 테스트가 끝나면 삭제하므로, 여기서 만드는 데이터는 실제 db.sqlite3(수동/운영 데이터)와
# 완전히 분리되어 있다. 업로드 테스트도 실제 media/ 디렉터리를 건드리지 않도록
# MEDIA_ROOT를 임시 디렉터리로 override한다.
TEST_MEDIA_ROOT = tempfile.mkdtemp(prefix='photoblog_test_media_')


@override_settings(MEDIA_ROOT=TEST_MEDIA_ROOT)
class PostApiTests(APITestCase):
    """GET/POST /api_root/Post/ 자동 검증 (새 최종 결과보고서 ID 6)."""

    POST_LIST_URL = '/api_root/Post/'

    REQUIRED_FIELDS = ['author', 'title', 'text', 'created_date', 'published_date', 'image']

    @classmethod
    def setUpTestData(cls):
        User = get_user_model()
        cls.author = User.objects.create_user(username='api_test_author', password='unused-pw')
        cls.post1 = Post.objects.create(
            author=cls.author,
            title='자동 테스트 게시물 1',
            text='자동 테스트용 본문 1',
        )
        cls.post2 = Post.objects.create(
            author=cls.author,
            title='자동 테스트 게시물 2',
            text='자동 테스트용 본문 2',
        )

    def test_get_post_list_returns_200(self):
        response = self.client.get(self.POST_LIST_URL)
        self.assertEqual(response.status_code, status.HTTP_200_OK)

    def test_get_post_list_returns_json_array(self):
        response = self.client.get(self.POST_LIST_URL)
        self.assertIsInstance(response.data, list)
        self.assertEqual(len(response.data), 2)

    def test_get_post_list_items_have_required_fields(self):
        response = self.client.get(self.POST_LIST_URL)
        for item in response.data:
            for field in self.REQUIRED_FIELDS:
                self.assertIn(field, item)

    def test_get_post_list_field_values_match_created_posts(self):
        response = self.client.get(self.POST_LIST_URL)
        titles = {item['title'] for item in response.data}
        self.assertIn(self.post1.title, titles)
        self.assertIn(self.post2.title, titles)

    def test_post_create_post_returns_201(self):
        payload = {
            'title': '새로 업로드된 게시물',
            'text': '새로 업로드된 본문',
        }
        response = self.client.post(self.POST_LIST_URL, payload)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)

    def test_post_create_post_reflects_title_and_text(self):
        payload = {
            'title': '필드 반영 확인용 제목',
            'text': '필드 반영 확인용 본문',
        }
        response = self.client.post(self.POST_LIST_URL, payload)
        self.assertEqual(response.data['title'], payload['title'])
        self.assertEqual(response.data['text'], payload['text'])

    def test_post_create_post_persists_to_database(self):
        payload = {
            'title': 'DB 저장 확인용 제목',
            'text': 'DB 저장 확인용 본문',
        }
        self.client.post(self.POST_LIST_URL, payload)
        self.assertTrue(Post.objects.filter(title=payload['title']).exists())

    def test_post_create_post_reflects_uploaded_image(self):
        from django.core.files.uploadedfile import SimpleUploadedFile

        # 1x1 투명 PNG (Pillow 없이도 유효한 ImageField 업로드가 되도록 실제 PNG 바이트 사용).
        png_bytes = (
            b'\x89PNG\r\n\x1a\n\x00\x00\x00\rIHDR\x00\x00\x00\x01\x00\x00\x00\x01'
            b'\x08\x06\x00\x00\x00\x1f\x15\xc4\x89\x00\x00\x00\nIDATx\x9cc\x00\x01'
            b'\x00\x00\x05\x00\x01\r\n-\xb4\x00\x00\x00\x00IEND\xaeB`\x82'
        )
        image = SimpleUploadedFile('test_image.png', png_bytes, content_type='image/png')
        payload = {
            'title': '이미지 업로드 확인용 제목',
            'text': '이미지 업로드 확인용 본문',
            'image': image,
        }
        response = self.client.post(self.POST_LIST_URL, payload, format='multipart')
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        self.assertTrue(response.data['image'])

        created = Post.objects.get(title=payload['title'])
        self.assertTrue(bool(created.image))

    def test_post_create_post_ignores_client_supplied_author(self):
        # author는 read_only_fields이므로 클라이언트가 보낸 값과 무관하게
        # 서버가 _resolve_default_author()로 결정한 작성자가 저장되어야 한다.
        other_author = get_user_model().objects.create_user(username='someone_else')
        payload = {
            'title': 'author 무시 확인용 제목',
            'text': 'author 무시 확인용 본문',
            'author': other_author.pk,
        }
        response = self.client.post(self.POST_LIST_URL, payload)
        self.assertEqual(response.status_code, status.HTTP_201_CREATED)
        created = Post.objects.get(title=payload['title'])
        self.assertNotEqual(created.author_id, other_author.pk)

    def test_post_create_post_missing_title_returns_400(self):
        payload = {
            'text': '제목 없이 보낸 본문',
        }
        response = self.client.post(self.POST_LIST_URL, payload)
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertIn('title', response.data)

    def test_post_create_post_missing_text_returns_400(self):
        payload = {
            'title': '본문 없이 보낸 제목',
        }
        response = self.client.post(self.POST_LIST_URL, payload)
        self.assertEqual(response.status_code, status.HTTP_400_BAD_REQUEST)
        self.assertIn('text', response.data)

    def test_get_single_post_detail_returns_200(self):
        response = self.client.get(f'{self.POST_LIST_URL}{self.post1.pk}/')
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(response.data['title'], self.post1.title)

    def test_get_nonexistent_post_detail_returns_404(self):
        response = self.client.get(f'{self.POST_LIST_URL}999999/')
        self.assertEqual(response.status_code, status.HTTP_404_NOT_FOUND)
