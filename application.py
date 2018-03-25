from flask import Flask, render_template, request
import xml.etree.ElementTree as et
import datetime
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
	
@app.route('/tum', methods = ['POST'])
def stream():
    tum = et.Element("TurfUpdateMessage")
    meta = et.SubElement(tum, "MetaData")
    req = et.SubElement(meta, "RequestedAt")
    req.text = str(datetime.datetime.now())
    tts = et.SubElement(tum, "TurfTumTransaction")
    userData = sqlite3.connect('db.db').execute("SELECT transactions from data where email='demo@turf.com'").fetchone()
    return userData
    
def GenerateData(limit):
    dateNow = datetime.datetime.now()
    Date = (dateNow.strftime("%m"),dateNow.strftime("%d"),dateNow.strftime("%Y"),dateNow.strftime("%A"))
    dateStamp = dateNow.strftime("%b")+" "+dateNow.strftime("%d")+" "+dateNow.strftime("%Y")
    timeStamp = (dateNow.strftime("%I"),dateNow.strftime("%M"),dateNow.strftime("%S"),dateNow.strftime("%p"))
    days = int(Date[1])
    NUM = monthrange(int(Date[2]),int(Date[0]))
    stream = (1,5,Date, timeStamp)
    daily = limit/int(NUM[1])

    for i in range(NUM[1]):
#       print("day "+str(i+1)+": ")
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
        
    return et.dump(tts)
        
        
 
def setupDB():
    if os.path.exists('db.db'):
        return
    else:
        c = sqlite3.connect('db.db')
        c.execute('''CREATE TABLE data
                    (email text PRIMARY KEY,
                    transactions text NOT NULL,
                    LastAccessed timestamp NOT NULL)''')
        data = createData()
        LastAcc = datatime.datetime(2011,4,7)
        c.execute('''INSERT into data VALUES ( 'demo@turf.com',{}, {}'''.format(data,LastAcc))
        c.commit()
        c.close()
       
        


def dateCheck(date1, date2, date3):
    #date1 is last login
    #date2 is current date
    #date3 is date being checked
    Date1 = date1.strftime("%b"+" "+"%d"+" "+"%Y")
    Date2 = date2.strftime("%b"+" "+"%d"+" "+"%Y")
    Date3 = date3.strftime("%b"+" "+"%d"+" "+"%Y")
    m1,d1,y1 = Date1.split()
    m2,d2,y2 = Date2.split()
    m3,d3,y3 = Date3.split()
    if int(y3)>int(y1):
        if int(y3) < int(y2):
            print("Date is within Date1 and Date2")
        elif int(d3)<=int(d2):
            print("Date is within Date1 and Date2")            
    elif int(d3)>=int(d1):
            print("Date is within Date1 and Date2")
    else:
        print("Date is not within the dates")
    return 1
 
    
if __name__=="__main__":
    app.run(host="155.246.213.67", port=55555)
