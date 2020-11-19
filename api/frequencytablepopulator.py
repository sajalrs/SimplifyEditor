import urllib.request
import mysql.connector

mydb = mysql.connector.connect(
    host="localhost",
    user="root",
    password="root",
    database="simplify"
)

mycursor = mydb.cursor()
sql = "INSERT INTO words (word, frequency) VALUES (%s, %s)"

url = "https://raw.githubusercontent.com/skywind3000/lemma.en/master/lemma.en.txt"
file = urllib.request.urlopen(url)

for line in file:
    decoded_line = line.decode("utf-8")
    if not (decoded_line[0:1] == ';'):
        wordfreq, definition = decoded_line.split('->')
        if ('/' not in wordfreq):
            word = wordfreq
            freq = 0
        else:
            word, freq = wordfreq.strip().split('/')

        words = [(word, freq)]
        synonyms = definition.strip().split(',')
        for synonym in synonyms:
            words.append((synonym, freq))

        mycursor.executemany(sql, words)
        print(words)

mydb.commit()
print(mycursor.rowcount, "record inserted.")
