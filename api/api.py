import flask
from flask import request, jsonify
import mysql.connector

app = flask.Flask(__name__)
app.config["DEBUG"] = True


@app.route('/', methods=['GET'])
def home():
    return "<h1>Word Frequency</h1><p>This site is a prototype API that returns the frequency of 187000 most used words as reported in the British National Corpus.</p>"


@app.route('/api/v1/resources/words/all', methods=['GET'])
def api_all():
    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="root",
        database="simplify"
    )


    cur = conn.cursor()
    cur.execute(query, to_filter)
    all_words = [dict((cur.description[i][0], value)
                      for i, value in enumerate(row)) for row in cur.fetchall()]
    return jsonify(all_words)


@app.errorhandler(404)
def page_not_found(e):
    return "<h1>404</h1><p>The resource could not be found.</p>", 404


@app.route('/api/v1/resources/words', methods=['GET'])
def api_filter():
    query_parameters = request.args

    id = query_parameters.get('id')
    word = query_parameters.get('word')
    frequency = query_parameters.get('frequency')

    query = "SELECT * FROM words WHERE"
    to_filter = []

    if id:
        query += ' id=%s AND'
        to_filter.append(id)
    if word:
        query += ' word=%s AND'
        to_filter.append(word)
    if frequency:
        query += ' frequency=%s AND'
        to_filter.append(frequency)
    if not (id or word or frequency):
        return page_not_found(404)

    query = query[:-4] + ';'

    conn = mysql.connector.connect(
        host="localhost",
        user="root",
        password="root",
        database="simplify"
    )


    cur = conn.cursor()
    cur.execute(query, to_filter)
    results = [dict((cur.description[i][0], value)
                    for i, value in enumerate(row)) for row in cur.fetchall()]
    return jsonify(results=results)

app.run(host='0.0.0.0')
