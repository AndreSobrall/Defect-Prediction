if [ "$#" -lt 2 ]; then
    echo "Not enough arguments [./opendiff <dataset> <bugs-dot-jar.branch>]"
else
	cd bugs-dot-jar/$1
	git checkout $2
	open .bugs-dot-jar/developer-patch.diff
	cd ../..
fi
