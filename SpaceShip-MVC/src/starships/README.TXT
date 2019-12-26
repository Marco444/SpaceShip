/** *************************************************************************************************************************************
  * The fact that I must use variables to model state can be "solved" through functional reactive programming. Using Signals, it would
  * be a chance to make some beautiful code and to learn more about this largely un-used technology.
  *
  * The only problem I've encountered with signals and alike is that they are rarely used in actual proyects, I am not saying it's
  * knwoledge you could not apply in a proyect in some way, but it does not seem viable to employ in a large-scale proyect.
  * ************************************************************************************************************************************/


/**
  * When picking up the project to review Scala and improve it, get a refresher by looking into the withinScreen() method of SpaceShips
  * and trying to solve it; when I feel confident, look at parts of the code and ask myself "How can I do it better?". A good starting
  * point can be looking for 'Bajo Acoplamiento, Alta Coesion.'
  *
  * Look into implementing the default Asteroid/Bullet addition to their corresponding lists when they are constructed. Can the same be
  * done with the line that takes them out of the list, ideally this would be done through a destructor, but Scala doesn't support them
  * (the JVM)
  **/