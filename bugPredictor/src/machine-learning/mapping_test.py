import os
from aux_func import list_directory
from aux_func import list_only_buggies_in_directory
import filecmp

# Path to Mapping output
DIR_PATH = "C:\\Users\\Andre\\Desktop\\ist\\Defect-Prediction\\bugPredictor\\output"

# colors
ANSI_RESET  = "\u001B[0m"
ANSI_RED    = "\u001B[31m"
ANSI_GREEN  = "\u001B[32m"
ANSI_YELLOW = "\u001B[33m"
ANSI_CYAN   = "\u001B[36m"

def find_broken_mappings(dataset_path, dataset_name, doPrintNames=False):
	
	# TODO: right now, it stores every non-diff files
	# slowly manually verify these diff's are worthing ignore or 
	# if the parser should be tweaked

	# ignore
	# mappings that are not considered errors
	# these depend on the context of the application
	to_ignore = []
	
	total  = 0
	broken = 0

	if(doPrintNames):
		print("Looking for Fixed and Buggy that have the same mapping (i.e. are broken):")
	
	# For each issue in the dataset
	for issue in list_directory(dataset_path):
		
		issue_path = dataset_path + "/" + issue
		
		if(doPrintNames):
			print(ANSI_CYAN + issue + ":" + ANSI_RESET)

		# for each buggy file present
		for file_name in list_only_buggies_in_directory(issue_path):
			# get file name after "buggy-"
			simpleFileName = file_name[6:]

			# They must preserve the path until now.
			buggy = issue_path + "/" + file_name
			# Get the correspondet fixed file path
			fixed = issue_path + "/" + "fixed-" + simpleFileName

			# print(fixed)
			# print(buggy)

			# The diff should be broken, or else, the mapping is broken.
			if(isNotDiff(buggy, fixed)):
				broken += 1
				if(doPrintNames):
					print(ANSI_YELLOW + "WARNING: " + file_name + ANSI_RESET)
				# will not be loaded
				to_ignore.append(buggy)

			elif(doPrintNames):
				print(ANSI_GREEN + "CORRECT" + ANSI_RESET)

			total += 1;

	if(doPrintNames):
		print("------------------------------------")
		print("Correct: ",dataset_name," :[",2*(total-broken),"/",2*total,"]")
		print("Broken:  ",dataset_name," :[",2*broken,"/",2*total,"]")
		print("------------------------------------")

	# Utilizado no shape do dataset
	good_issues = 2 * (total-broken) - 1

	return (to_ignore, good_issues)


# Compara o conteudo de dois, retorna "true" se forem iguais.
def isNotDiff(fixed, buggy):
	return filecmp.cmp(fixed, buggy, shallow=False)


def main():
	find_broken_mappings(True)

if __name__ == "__main__":
    main()
