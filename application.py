from flask import Flask, render_template, request

app = Flask(__name__)

#First route
@app.route('/', methods = ['POST','GET'])
def index():
	string = "HELLO WORLD"
	return(string)

#Second route
@app.route('/next', methods = ['POST','GET'])
def cool():
	newString = "HEHEHEHE"
	return(newString)
	
if __name__=="__main__":
	app.run()
