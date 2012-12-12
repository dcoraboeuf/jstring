# Sample localized strings for unit tests

list.separator.prefix
	en,fr -> -\u0020
list.separator.suffix
	en,fr -> \n

[general]
# General section

app.title
	# Application title
	en -> My favourite application
	fr -> Mon application préférée

[home]
# Home page

home.title
	# Home page title
	en -> Home page
	fr -> Page d'accueil
	
home.message
	# Message to display on the home page
	# This is a very long message
	en -> ...
		This is a very long message that spans on
		several lines.
		# Comments are supported in the middle of the text
		Carriage returns are introduced using the normal \n Java
		escaping. Other ones are ignored.
		## The # character must be escaped at the beginning.
		...
	fr -> ...
		Ceci est un très long message qui s'étend
		sur plusieurs lignes.
		Les retours chariot sont notifiés grâce au \n Java. Les
		autres sont ignorés.	
		## Le caractère # doit être doublé au début d'une ligne.
		...
		
home.info
	# Placeholders are supported by name
	en -> Application version: {version}, build: {build}, {{not a parameter}}
	fr -> Version de l'application: {version}, build: {build}, {{pas un paramètre}}
	