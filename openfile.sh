
if [ "$#" -lt 3 ]; then
    echo "Not enough arguments [./opendiff <dataset> <bugs-dot-jar.branch> <filepath>]"
else
	cd bugs-dot-jar/$1
	git checkout $2
	open /Users/andre/Desktop/Tese/$3
	cd ../..
fi
