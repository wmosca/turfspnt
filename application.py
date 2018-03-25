from flask import Flask, render_template, request
import sqlite3
import os

app = Flask(__name__)

#First route
@app.route('/', methods = ['POST','GET'])
def index():
    string = "HELLO WORLD"
    return(string)

@app.route("/login", methods=['POST'])
def login():
    login_form = request.form
    print("received: {}".format(login_form))

#Second route
@app.route('/next', methods = ['POST','GET'])
def cool():
    newString = "HEHEHEHE"
    return(newString)
	
if __name__=="__main__":
    app.run(host="155.246.213.67", port=55555)
