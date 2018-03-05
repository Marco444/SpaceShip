Main problems:

    1. Weird thing happens if one stop clicking the mouse. Bullets and Asteroid start to jiggle in their place. This
       is in addition to the more than slow performance of a simple game. SOLVED ! :)
    2. Both asteroids and bullets seem to give an absolute f$ck about their initial position. SOLVED :)
    3. Finally, I need to work out the CollisionEngine class given the perfomance bottleneck I already encountered,
       and that the implementation in MovingObject does not work ... :) SOLVED :)

To Do:
    1.Separacion MVC (Model-view-controller), basicamente es separar tu codigo en lo que es modelo de datos (Naves, asteroides, balas, puntos),
      vista (la logica de pintar los componentes del modelo) y controladores (Que se encargan de unir los dos acteriores,
      manejar la logica del juego,los eventos del usuario entre otros)

    2.RxJava (https://github.com/ReactiveX/RxJava) es una libreria en Java bastate popular para reactive programing,
      vas a encontrar mucho hecho con esta libreria a diferencia de Signals. Pero si no me equivoco muchos de los conceptos
      son lo mismo. Si tengo tiempo en algun momento te preparo un ejemplo y te lo mando.

    3.Y como herramientas para empezar a programar un poco mas facil y cómodo podes mirar Git, que es un manejador de versiones,
     te permite ir guardando tu codigo con los distintos cambios que le hacer, mantener el histrial de todos los archivos, probar
      cosas sin riego a perder lo que tenias hasta ahora. Es una herramienta super grande, pero con aprender lo minimo que son commits
       y branches ya te sirve un montón!

    4.Otra cosa muy buena para todo esto es usar SBT (U otros como maven, gradle) que son herramintas para manejar las distintas
    cosas que se necesitan para que un projecto funcione. Cosas como como compilar, manejar las dependencias (Librerias) entre
    otras cosas. Esto es un poco mas complejo, pero tambien con que aprendas lo basico si tenes tiempo es suficiente.

