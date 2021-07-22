from django.urls import path

from . import views

urlpatterns = [
    path('', views.get_stack_result, name="stack_result"),
]