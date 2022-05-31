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
from django.urls import path, include
from sitio import views
from django.views.generic.base import RedirectView

urlpatterns = [
    path('admin/', admin.site.urls),
    path('home/', views.home),
    path('escuchar/', views.escuchar, name= 'homepage'),
    path('creaLista/', views.creaLista),

    # Api
    path('cancion/', views.getCancion),
    path('putLista/', views.putLista),
    path('getListasBD/', views.getListas),

    # For google.
    path('accounts/', include('allauth.urls')),

	# path('accounts/profile/', RedirectView.as_view(pattern_name='homepage', permanent=False)),
    # path('accounts/profile/', views.profile),

    path('muestra_usuarios/',views.muestra_usuarios),

	path('', views.index),

	path('drive/', views.drive),
]

handler400 = 'sitio.views.error400'
handler403 = 'sitio.views.error403'
handler404 = 'sitio.views.error404'
handler500 = 'sitio.views.error500'