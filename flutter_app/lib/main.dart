import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';


void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final appTitle = 'Flutter Basic List Demo';

    return MaterialApp(
      title: appTitle,
      home: Scaffold(
        appBar: AppBar(
          title: Text(appTitle),
        ),
        body: ListView(
          children: <Widget>[
            ListTile( title: Text('Apple'),onTap:() => _showToast(context,"Apple is clicked"),),
            ListTile( title: Text('Orange'),onTap:() => _showToast(context,"Orange is clicked"),),
            ListTile(title: Text('Banana'),onTap:() => _showToast(context,"Banana is clicked"),),
            ListTile( title: Text('Papaya'),onTap:() => _showToast(context,"Papaya is clicked"),),
            ListTile( title: Text('Mango'),onTap:() => _showToast(context,"Mango is clicked"),),
          ],
        ),
      ),
    );
  }
  _showToast(BuildContext context,String string) {
    Fluttertoast.showToast(msg:string);
  }
}

