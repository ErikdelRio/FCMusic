from django.shortcuts import render
from django.http import HttpResponse

from .models import Cancion,Lista_reproduccion,Cancion_lista_reproduccion
import json

# Create your views here.
def parseRequest(req):
    body_unicode = req.body
    body = json.loads(body_unicode)
    return body

    
def home(request):
    listas = Lista_reproduccion \
        .objects \
        .filter(usuario_id=request.user.id)

    return render(request, 'home.html', {'listas': listas})

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

def getCancionesLista(request):
    body_unicode = request.body
    body = json.loads(body_unicode)

    user_id = request.user.id
    lista_id = body['lista_id']
    res = Cancion_lista_reproduccion.objects \
        .filter(Lista_reproduccion=lista_id) \
        .selectRelated('Cancion')

    return list(res)
    # return render(request, )

def escucharLista(request):
    lista = getCancionesLista(request)
    # listas = Lista_reproduccion \
    #    .objects \
    #    .filter(usuario_id=request.user.id) \
    #    .selectRelated('Cancion')

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

    i = 0
    for c in body['canciones']:
        nuevaCancionLista = Cancion_lista_reproduccion()
        nuevaCancionLista.cancion_id = c.cancion_id
        nuevaCancionLista.indice = i
        nuevaCancionLista.save()
    
    return HttpResponseRedirect('/home')
