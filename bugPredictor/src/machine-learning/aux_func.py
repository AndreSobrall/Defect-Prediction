import os

# Path to Mapping output
DIR_PATH = "/Users/andre/Desktop/Tese/bugPredictor/output"

# Retorna listagem do diretorio em 'path'
def list_directory(path):
	directory = os.listdir(path)

	# Caso especial.
	if "max_size.txt" in directory:
		directory.remove("max_size.txt")

	if ".DS_Store" in directory:
		directory.remove(".DS_Store")

	directory.sort()
	
	return directory

# Retorna listagem de apenas os ficheiros buggy
def list_only_buggies_in_directory(path):	
	directory = list_directory(path)
	return [ x for x in directory if "fixed" not in x ]

