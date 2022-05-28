"""FCMusic URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.conf.urls import handler400,handler404,handler403,handler500
from django.contrib import admin
from django.urls import path
from sitio import views

urlpatterns = [
    path('admin/', admin.site.urls),
    path('home/', views.home),
    path('escuchar/', views.escuchar),
    path('creaLista/', views.creaLista),
    path('putLista/', views.putLista),

    path('cancion/', views.getCancion)
]

handler400 = 'sitio.views.error400'
handler403 = 'sitio.views.error403'
handler404 = 'sitio.views.error404'
handler500 = 'sitio.views.error500'
