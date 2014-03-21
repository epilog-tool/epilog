#!/bin/sh

# config: set source doc and output
USR=user-documentation
DOCS="$USR"
OUT=html

function die(){
 echo $1
 exit 1
}

# make sure that it works when invoked from an other directory
cd `dirname $0`

# check for required commands and remove old output
command -v xsltproc   > /dev/null || die "Command 'xsltproc' not found"
command -v yelp-build > /dev/null || die "Command 'yelp-build' not found"
test -e $OUT && echo "Confirm to remove '$OUT'..." && rm -r "$OUT"
mkdir $OUT

for DOC in $DOCS
do
	cd $DOC
	echo Generating $DOC ...
	
	# generate biblio page if needed
	if [ -e biblio.xml ]
	then
		xsltproc -o biblio.page ../biblio.xslt biblio.xml
	fi
	
	# generate HTML version of the documentation
	mkdir ../$OUT/$DOC
	yelp-build html -x "../custom.xslt" -o ../$OUT/$DOC *.page

	# cleanup
	test -e biblio.xml && test -e biblio.page && rm biblio.page
	cd ..
done

