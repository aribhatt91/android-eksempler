cd ~/android/android-eksempler/AndroidElementer
svn up
cd assets/
svn up
svn delete src AndroidManifest.xml
svn commit -m "Ny kildekode"
svn up
svn copy ../src ../AndroidManifest.xml .
svn commit -m "Ny kildekode"

