import eyed3

def imprime_cancion(cancion):
    audio = eyed3.load(cancion)

    if not audio.tag:
        audio.initTag()

    print("-----")
    print("Título:",audio.tag.title)
    print("Artista:",audio.tag.artist)
    print("Álbum:",audio.tag.album)
    print("Artista del álbum:",audio.tag.album_artist)
    print("Compositor:",audio.tag.composer)
    print("Editor:",audio.tag.publisher)
    print("Fecha de Lanzamiento:",audio.tag.release_date)
    if audio.tag.genre:
        print("Género:",audio.tag.genre.name)


def cancion_dic(cancion):
    try:
        audio = eyed3.load(cancion)
    except IOError:
        return {}
    if audio.tag is None:
        return {}
    
    titulo = audio.tag.title
    if titulo is None:
        titulo = 'Indefinido'

    artista = audio.tag.artist
    if artista is None:
        artista = 'Indefinido'

    album = audio.tag.album
    if album is None:
        album = 'Indefinido'

    artista_del_album = audio.tag.album_artist
    if artista_del_album is None:
        artista_del_album = 'Indefinido'

    compositor = audio.tag.composer
    if compositor is None:
        compositor = 'Indefinido'

    editor = audio.tag.publisher
    if editor is None:
        editor = 'Indefinido'

    fecha = audio.tag.release_date
    if fecha is None:
        fecha = 'Indefinido'

    generos = audio.tag.genre
    if generos is None:
        lista_generos = []
    else:
        lista_generos = generos.name.split(';')

    return {'Título':titulo,
            'Artista':artista,
            'Álbum':album,
            'Artista del álbum':artista_del_album,
            'Compositor':compositor,
            'Editor':editor,
            'Fecha':fecha,
            'Géneros':lista_generos
            }



print(cancion_dic('canciones/Placebo - Every You Every Me.mp3'))
