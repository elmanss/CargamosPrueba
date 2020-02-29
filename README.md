# CargamosPrueba
Prueba tecnica Cargamos.com

Este aplicativo es una prueba técnica para evaluación por parte de Cargamos.com, el proyecto consiste en un catálogo de películas con scroll infinito, el cual al hacer clic lanza una vista con el detalle de la película seleccionada.

Dentro del detalle el usuario puede agregar la película a una lista de favoritas, almacenada localmente en el aplicativo usando la librería SqlDelight de Square.

Al hacer clic el menú superior en la vista principal, el usuario puede acceder a la lista de favoritos antes mencionada. Esta pantalla es similar a la principal, pero cambia el scroll infinito por la opción del filtrado por título. Al hacer clic en un poster, se lanza la vista de detalle, únicamente que la opción que se le brinda al usuario es la de eliminar la película de sus favoritas. Al regresar a la vista anterior se refresca la lista, dejando de mostrar el poster correspondiente a la película eliminada.

Se usa el patrón Model-View-Presenter para separación de lógica de negocio, datos e interfaz gráfica. Las llamadas a Red, Base de datos, o preferencias se llevan a cabo por medio de Interactors, que mediante clases Mapper crean modelos de negocio a partir de las entidades obtenidas desde la capa de datos.

Las llamadas a red y base de datos se llevan acabo exclusivamente usando RxJava con Retrofit y SqlDelight para Red y Base de datos, respectivamente.
