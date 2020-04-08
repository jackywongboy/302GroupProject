from flask import Flask, render_template, url_for, request, session, redirect
import cx_Oracle

conn = cx_Oracle.connect('stu038/cestu038@144.214.177.102/xe')

cur = conn.cursor()


app = Flask(__name__)
app.secret_key = 'abc'


@app.route("/index")
@app.route("/")
def index():
    if 'id' in session:
        return redirect(url_for('orderfood'))
    cur.execute("SELECT shopid, shopname, companyid, shop_imgLink FROM FP_shop")

    shop = cur.fetchall()
    noshop = len(shop)
    cur.execute('commit')
    return render_template('home.html', shop=shop, noshop=noshop)

@app.route("/logout")
def logout():
    session.pop('id', None)
    return redirect(url_for('index'))


# The page after log in
@app.route('/orderfood', methods=['POST', 'GET'])
def orderfood():
    if request.method == 'POST':
        session.pop = ('id', None)
        UserID = request.form["UserID"]
        pwd = request.form['pwd']

        cur.execute("SELECT CustomerID, Customer_NAME, PWD, ADDRESS FROM FP_Customer WHERE CustomerID= %s" % UserID)

        result = cur.fetchone()
        cur.execute('commit')
        password = result[2]

        if pwd == password:
            session['id'] = UserID
            cur.execute("SELECT shopid, shopname, companyid, shop_imgLink FROM FP_shop")

            shop = cur.fetchall()
            noshop = len(shop)
            cur.execute('commit')
            return render_template('orderfood.html', result=result, shop= shop, noshop=noshop)
        return redirect(url_for('index'))
    elif 'id' in session:
        cur.execute("SELECT CustomerID, Customer_NAME, GENDER, ADDRESS, PHONE, EMAIL, PWD FROM FP_Customer WHERE "
                    "CustomerID= %s" % (session['id']))
        result = cur.fetchone()
        cur.execute('commit')
        cur.execute("SELECT shopid, shopname, companyid, shop_imgLink FROM FP_shop")

        shop = cur.fetchall()
        noshop = len(shop)
        cur.execute('commit')
        return render_template('orderfood.html', result=result, shop= shop, noshop=noshop)

    return redirect(url_for('index'))


# showing the food menu of that shop
@app.route('/detail', methods=['POST', 'GET'])
def detail():
    if 'id' in session:
        if request.method == 'POST':
            shopid = request.form["shopid"]
            session["shopid"] = shopid

            cur.execute("SELECT shopid, shopname, companyid, shop_imgLink FROM FP_shop where shopid= %s" % shopid)
            result2 = cur.fetchone()

            cur.execute("SELECT food_id, food_name, food_category, food_price, food_imgLink FROM FP_food")
            result = cur.fetchall()

            cur.execute("SELECT s_id, s_category, s_name, s_price FROM FP_SPECIAL_ITEM")
            result3 = cur.fetchall()

            cur.execute('commit')
            Number = len(result)
            return render_template('detail.html', result=result, Number=Number, result2=result2, result3=result3)
        else:
            cur.execute("SELECT food_id, food_name, food_category, food_price FROM FP_food")
            result = cur.fetchall()
            cur.execute('commit')
            Number = len(result)
            return render_template('detail.html', result=result, Number=Number)
    else:
        shopid = request.form["shopid"]

        cur.execute("SELECT shopid, shopname, companyid, shop_imgLink FROM FP_shop where shopid= %s" % shopid)

        result2 = cur.fetchone()

        cur.execute("SELECT food_id, food_name, food_category, food_price, food_imgLink FROM FP_food")
        result = cur.fetchall()
        cur.execute('commit')
        Number = len(result)
        return render_template('detail2.html', result=result, Number=Number, result2=result2)


# showing account details
@app.route('/account')
def account():
    if 'id' in session:
        cur.execute("SELECT CustomerID, Customer_NAME, GENDER, PHONE, EMAIL, ADDRESS, PWD FROM FP_Customer WHERE "
                    "CustomerID= %s" % (session['id']))
        result = cur.fetchone()
        cur.execute('commit')
        return render_template('account.html', result=result)
    return redirect(url_for('index'))

# allow user change account information
@app.route('/changeinfor')
def changeinfor():
    if 'id' in session:
        cur.execute("SELECT CustomerID, Customer_NAME, GENDER, PHONE, EMAIL, ADDRESS, PWD FROM FP_Customer WHERE "
                    "CustomerID= %s" % (session['id']))

        result = cur.fetchone()
        cur.execute('commit')
        return render_template('changeinfor.html', result=result)
    return render_template('home.html')


# For customers to select payment and delivery method
@app.route('/payment')
def payment():
    cur.execute("SELECT shopid, shopname FROM FP_shop")
    shop = cur.fetchall()
    noshop = len(shop)
    cur.execute('commit')
    return render_template('Payment.html', shop=shop, noshop=noshop)


# update user information in db
@app.route('/changed', methods=['POST', 'GET'])
def changed():
    if 'id' in session:
        name = request.form["UserName"]
        email = request.form["email"]
        address = request.form["address"]
        phone = int(request.form["phone"])
        pwd = request.form['pwd']

        cur.execute("Update FP_Customer Set Customer_NAME ='%s' , PHONE='%d',"
                    " EMAIL='%s', ADDRESS='%s', PWD='%s'" 
                    "Where CustomerID= %s" % (
                        name, phone, email, address, pwd, session['id']))
        cur.execute('commit')

        return redirect(url_for('account'))


@app.route('/register')
def register():
    return render_template('register.html')


@app.route('/success', methods=['POST', 'GET'])
def success():
    error = None
    if request.method == 'POST':
        name = request.form["UserName"]
        email = request.form["email"]
        address = request.form["address"]
        gender = request.form["gender"]
        phone = int(request.form["phone"])
        pwd = request.form['pwd']

        cur.execute("INSERT INTO FP_CUSTOMER (CUSTOMERID, CUSTOMER_NAME, GENDER, PHONE, EMAIL, ADDRESS, PWD) VALUES ("
                    "customer_sequence.nextval, '%s','%s', '%d', '%s', '%s', '%s') " % (
                        name, gender, phone, email, address, pwd))

        cur.execute('commit')

        cur.execute("SELECT CustomerID, Customer_NAME FROM FP_Customer WHERE email= '%s'" % email)
        result = cur.fetchone()
        cur.execute('commit')
        ID = result[0]

        return render_template('success.html', accountid=ID, Name=name, Gender=gender,
                               Email=email, Phone=phone, Address=address, error=error)


@app.route('/qwe', methods=['POST'])
def qwe():
    if request.method == 'POST':
        names = request.get_json()
        s = str(names)
        session['sc_detail'] = (s[11:-2])
        print('Sucess'+s[11:-2])
        return redirect(url_for('confirm'))

    else:
        return redirect(url_for('index'))


@app.route("/confirm")
def confirm():
    if 'sc_detail' in session:
        cur.execute("SELECT CustomerID, Customer_NAME, ADDRESS, PHONE FROM FP_Customer WHERE "
                    "CustomerID= %s" % (session['id']))
        result = cur.fetchone()

        cur.execute("SELECT SHOPNAME, SHOPADDRESS,SHOPTELPHONE FROM FP_SHOP WHERE "
                    "SHOPID= %s" % (session['shopid']))
        result2 = cur.fetchone()

        cur.execute('commit')

        return render_template('confirm.html',result=result,result2=result2)

    else:
        print('nonono')
        return redirect(url_for('index'))

@app.route('/confirmed', methods=['POST'])
def confirmed():
    if request.method == 'POST':
        qwe = session['sc_detail']

        print('session yes')
        print(qwe)

        itemArray = eval("{"+qwe+"}")

        print(itemArray)

        # Common Details
        com = {}
        com['type']=1    #1 type only
        com['shop']="1"  # 1,2,3,4 selections
        com['area']=1    #0=Dine in, 1=Takeout

        # Add Food Details

        jsondata = {}
        food={}
        ot={}

        no=0
        for i in itemArray:
            if itemArray[i]['type'] == 'f':
                d={}
                z={}
                food= {}
                
                time=i
                fid=itemArray[i]['fid']
                special=[]   
                s=itemArray[i]['sid']
                
                d['fid'] = fid  
                d['sid'] = s
                d['time'] = time
                
                food['f'+str(no)]= [d]
                if no == 0:
                    jsondata['food'] = food
                else:
                    jsondata['food'] = {**jsondata['food'], **food}
                no=no+1

        sno=0
        for i in itemArray:
            if itemArray[i]['type'] == 'c':
                dd = {}
                foods = {}
                
                cid=itemArray[i]['cid']
                fid=itemArray[i]['fid']
                time=i

                dd['cid'] = cid
                dd['fid'] = fid
                dd['time'] = time

                foods['c'+str(sno)]= [dd]
                
                if sno == 0:
                    jsondata['com'] = foods
                else:
                    jsondata['com'] = {**jsondata['com'], **foods}
                sno=sno+1        

        jsondata = {**com, **jsondata}

        from send import sent
        oid = sent(jsondata)

        from random import randrange
        gg = "05827634"+str(randrange(10))+str(randrange(10))

        return render_template("orderconfirmed.html",oid=oid,gg=gg)

    else:
        return 'error'

if __name__ == '__main__':
    app.run(debug=True)
