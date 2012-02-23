cd ~/android/android-eksempler/AndroidElementer
svn up
cd assets/
svn up
svn delete src AndroidManifest.xml
svn commit -m "Ny kildekode"
svn up
svn copy ../src ../AndroidManifest.xml .
svn commit -m "Ny kildekode"

cd ..
ant clean
cd ..
rm -f AndroidElementer.zip
zip -9r AndroidElementer.zip AndroidElementer
scp AndroidElementer.zip javabog.dk:javabog.dk/filer/android/
