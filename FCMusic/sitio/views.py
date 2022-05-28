from django.shortcuts import render
from django.http import HttpResponse

from .models import Cancion,Lista_reproduccion,Cancion_lista_reproduccion
import json
from django.core import serializers

# Create your views here.
def parseRequest(req):
    body_unicode = req.body
    body = json.loads(body_unicode)
    return body


def home(request):
    listas = Lista_reproduccion \
        .objects \
        .filter(usuario_id=request.user.id)

    # json_res = serializers.serialize('json', listas)
    return render(request, 'home.html', {'listas': listas})

def escuchar(request):
    return render(request, 'cancion/escuchar.html')

def getCancion(request):
    body_unicode = request.body
    body = json.loads(body_unicode)

    titulo_req = body['titulo']
    result = list(Cancion.objects.filter(titulo__icontains=titulo_req))

    json_res = serializers.serialize('json', result)
    return HttpResponse(json_res)

def getCancionesLista(request):
    body_unicode = request.body
    body = json.loads(body_unicode)

    user_id = request.user.id
    lista_id = body['lista_id']
    res = Cancion_lista_reproduccion.objects \
        .filter(Lista_reproduccion=lista_id) \
        .selectRelated('Cancion')

    return list(res)

def escucharLista(request):
    lista = getCancionesLista(request)

    return render(request, 'cancion/escuchar.html', {'lista': lista})

def creaLista(request):
    return render(request, 'cancion/creaLista.html')

def putLista(request):
    body = parseRequest(request)
    nuevaLista = Lista_reproduccion()
    nuevaLista.usuario_id = request.user.id
    nuevaLista.estado_publico = body['estado']
    nuevaLista.nombre = body['nombre']
    nuevaLista.save()

    i = 1
    for c in body['canciones']:
        nuevaCancionLista = Cancion_lista_reproduccion()
        nuevaCancionLista.lista_id = nuevaLista.id
        nuevaCancionLista.cancion_id = c.cancion_id
        nuevaCancionLista.indice = i
        nuevaCancionLista.save()
        i+=1

    return HttpResponseRedirect('/home')
