cd ~/android/android-eksempler/AndroidElementer
svn up
cd assets/
svn up
svn delete src
svn commit -m "Ny kildekode"
svn up
svn copy ../src .
svn commit -m "Ny kildekode"

