from django.shortcuts import render
from django.http import HttpResponse
from .models import Cancion
import json
from social_django.models import UserSocialAuth

# Create your views here.
def escuchar(request):
    return render(request, 'cancion/escuchar.html')

def getCancion(request):
    body_unicode = request.body
    body = json.loads(body_unicode)

    titulo_req = body['titulo']
    result = list(Cancion.objects.filter(titulo__icontains=titulo_req))

    canciones = {
        "canciones": result
    }
    return HttpResponse(json.dumps(canciones))
    # return render(request, json.dumps(canciones))

def creaLista(request):
    return render(request, 'cancion/creaLista.html')

def profile(request):
	return render(request, 'cancion/profile.html')

def muestra_usuarios(request):
    # select_related for performance.
    google_logins = UserSocialAuth.objects.select_related("user").filter(provider="google-oauth2")
    for google_login in google_logins:
        print(google_login.user.pk, google_login.user.email)
    return HttpResponse("""<html><script>window.location.replace('/');</script></html>""")