# Sample localized strings for unit tests

# General section
[general]

# Application title
app.title
	en -> My favourite application
	fr -> Mon application préférée

# Home page
[home]

# Home page title
home.title
	en -> Home page
	fr -> Page d'accueil
	
# Message to display on the home page
home.message
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
		
# Placeholders are supported by name
home.info
	en -> Application version: {version}, build: {build}, {{not a parameter}}
	