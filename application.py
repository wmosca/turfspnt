from flask import Flask, render_template, request
import xml.etree.ElementTree as et
import random as rd
from calendar import monthrange
import datetime
import sqlite3
import os

vendors = [("Acme Supermarkets", "Hoboken, NJ, USA"),
           ("JP Morgan Chase", "New York, NY, USA"),
           ("Bolshik", "Moscow, Russia"),
           ("ASDf", "ABC, NJ, USA")]

app = Flask(__name__)

#First route
@app.route('/', methods = ['POST','GET'])
def index():
    string = "HELLO WORLD"
    return(string)

@app.route("/login", methods=['POST'])
def login():
    login_form = request.form

#Second route
@app.route('/next', methods = ['POST','GET'])
def cool():
    newString = "HEHEHEHE"
    return(newString)
	
@app.route('/tum', methods = ['POST'])
def stream():
    tum = et.Element("TurfUpdateMessage")
    meta = et.SubElement(tum, "MetaData")
    req = et.SubElement(meta, "RequestedAt")
    req.text = str(datetime.datetime.now())
    tts = et.SubElement(tum, "TurfTransactionStream")
    user_data = sqlite3.connect('db.db', detect_types=sqlite3.PARSE_DECLTYPES).execute("SELECT transactions from data where email='demo@turf.com'").fetchone()
    last_checked = sqlite3.connect("db.db", detect_types=sqlite3.PARSE_DECLTYPES).execute("SELECT LastAccessed from data where email='demo@turf.com'").fetchone()
    current = datetime.datetime.now()
    done = False
    user_data = et.fromstring(user_data[0])
    for t in user_data:
        for a in t:
            if a.tag == "Date":
                t_date = a.text
                if dateCheck(last_checked[0], current, t_date):
                    tts.append(t)
                    break
                else:
                    done = True
        if done:
            break
    updateTum()
    return et.tostring(tum)
    
def GenerateData(limit):
    V = len(vendors)
    List = []
    dateNow = datetime.datetime.now()
    Date = (dateNow.strftime("%m"),dateNow.strftime("%d"),dateNow.strftime("%Y"),dateNow.strftime("%A"))
    dateStamp = dateNow.strftime("%b")+" "+dateNow.strftime("%d")+" "+dateNow.strftime("%Y")
    timeStamp = (dateNow.strftime("%I"),dateNow.strftime("%M"),dateNow.strftime("%S"),dateNow.strftime("%p"))
    days = int(Date[1])
    NUM = monthrange(int(Date[2]),int(Date[0]))
    stream = (1,5,Date, timeStamp)
    daily = limit/int(NUM[1])

    for i in range(NUM[1]):
        for j in range(rd.randint(0,5)):
            x = round(rd.uniform(0,daily),2)
            vend, locat = vendors[rd.randint(0,V-1)]
            dateStamp = dateNow.strftime("%b")+" "+str(i+1)+" "+dateNow.strftime("%Y")
            timeS = datetime.time(1,1,1)
            TIME = (timeS.strftime("%H")+" "+timeS.strftime("%M")+" "+timeS.strftime("%S"))
            delta = datetime.timedelta(minutes = rd.randint(1,5)*60)
            current = datetime.datetime(2018,3,24,8,40,0)+delta
            currentTime = (current.strftime("%I")+":"+current.strftime("%M")+":"+current.strftime("%S")+" "+current.strftime("%p"))
            DatStream = (rd.randint(0,1),x,dateStamp,currentTime,vend,locat)
            List.append(DatStream)
    return List
  
def createData():
    tts = et.Element("TurfTransactionStream")
    for t in GenerateData(3000):
        Tran = et.SubElement(tts, "TurfTransaction")
        sign = et.SubElement(Tran, "Sign")
        amount = et.SubElement(Tran,  "Amount")
        date = et.SubElement(Tran, "Date")
        time = et.SubElement(Tran, "Time")
        vendor = et.SubElement(Tran, "Vendor")
        location = et.SubElement(Tran, "Location")
        sign.text = str(t[0])
        amount.text = str(t[1])
        date.text = str(t[2])
        time.text = str(t[3])
        vendor.text = str(t[4])
        location.text = str(t[5])
    return et.tostring(tts)
        
 
def setupDB():
    if os.path.exists('db.db'):
        return
    else:
        c = sqlite3.connect('db.db', detect_types=sqlite3.PARSE_DECLTYPES)
        c.execute('''CREATE TABLE data
                    (email text PRIMARY KEY,
                    transactions text NOT NULL,
                    LastAccessed timestamp NOT NULL)''')
        data = createData()
        LastAcc = datetime.datetime(1900,3,3)
        c.execute("INSERT into data VALUES ('demo@turf.com',?, ?)", (data, LastAcc))
        c.commit()
        c.close()
 
 
def updateTum():
    c = sqlite3.connect('db.db', detect_types=sqlite3.PARSE_DECLTYPES)
    c.execute("UPDATE data set LastAcc = ?",(datetime.datetime.now())


def dateCheck(date1, date2, date3):
    #date1 is last login
    #date2 is current date
    #date3 is date being checked
    d3 = date3.split(" ")
    d3_day = int(d3[1])
    d3_year = int(d3[2])
    d2_day = int(date2.day)
    d2_year = int(date2.year)
    d1_day = int(date1.day)
    d1_year = int(date1.year)
    if d1_year == 1900 and d3_day <= d2_day:
        return True
    else:
        if d3_day > d1_day and d3_day <= d2_day:
            return True
    return False
    #Date1 = date1.strftime("%b"+" "+"%d"+" "+"%Y")
    ##Date2 = date2.strftime("%b"+" "+"%d"+" "+"%Y")
    #d3 = date3.split(" ")[1:]
    #d3_day = d3[1]
    #d3_year = d3[2]
    #m1,d1,y1 = Date1.split()
    #m2,d2,y2 = Date2.split()
    #m3,d3,y3 = Date3.split()
    #if int(y3)>int(y1):
    #    if int(y3) < int(y2):
    #        return True
    #    elif int(d3)<=int(d2):
    #        return True
    #elif int(d3)>=int(d1):
    #        return True
    #else:
    #    return False
 
    
if __name__=="__main__":
    setupDB()
    app.run(host="155.246.213.67", port=55555)
