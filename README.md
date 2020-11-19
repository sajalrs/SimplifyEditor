# SimplifyEditor
 Simplify editor is a text editor that flags uncommon words as they are typed to discourage the use of jargon. 
 It is a full stack mobile application that uses Python, Flask and MySQL on the back-end and a Kotlin and XML android application on the front-end.
</br>
</br>
<p align="center">
<img src="https://i.imgur.com/ikM5Lnt.jpg" width="250">
</br>
</br>
Words that aren't commenly used are highlighted in red
</p>
</br>
</br>
 Python script /api/frequencytablepopulator.py was used to scrape the British National Corpus for words and their frequency of use, in the English language,
 to populate a MySQL table using that data. The schema for the table is defined in /api/tablecreator.php. A Flask REST API /api/api.py, upon receiving a request, scans 
 the MySQL database for the queried word and returns its frequency using the MySQL python connector.   
</br>
</br>
<p align="center">
<img src="https://i.imgur.com/OhFrwrS.jpg" width="250">
</br>
</br>
To no one's surprise nursery rhymes mostly contain common words
</p>
</br>
</br>
On the front end, the android application makes requests to the REST API as words are typed and highlights the word if its frequency is lower than a predefined constant.
Future iterations of the app can have a slider to determine the level of complexity of these words. The android application uses the Retrofit library to make the api calls and 
gson to convert the JSON objects into Kotlin data class objects.
</br>
</br>


