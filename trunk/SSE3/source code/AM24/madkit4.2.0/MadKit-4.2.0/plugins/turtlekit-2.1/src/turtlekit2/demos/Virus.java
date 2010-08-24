/*
* Virus.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2007 Fabien Michel
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package turtlekit2.demos;

import java.awt.Color;

import madkit.kernel.Message;
import turtlekit2.kernel.agents.Turtle;
/** An infected turtle transmits virus by sending a real agent message
	to the turtles who cross its way

  @author Fabien MICHEL
  @version 1.1 6/12/1999 */


@SuppressWarnings("serial")
 public class Virus extends Turtle 
 {
	boolean infected;



 public Virus()
 {
 }

 public void setup()
 {
	 String inf = getAttributes().getString("infection");
	 if (inf.equals("red")) infected = true;
		else infected = false;
	if (infected)
	{
		playRole("infected");
		setColor(Color.red);
	}
	else
		 setColor(Color.green);
	
	
	
		try {
			if(inf.equals("red")) setNextAction(getClass().getMethod("red"));
			else
			setNextAction(getClass().getMethod("green"));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
	
 }


void wiggle()
{
	fd(1);
	turnRight(Math.random()*60);
	turnLeft(Math.random()*60);
}     

 

 public String green()
  {
  if (nextMessage() != null)
  {
	  setColor(Color.red);
	  playRole("infected");
	  return ("red");
  }
  else
  {
	  wiggle();
	  return ("green");
  }

 }



 public String red()
  {
	 wiggle();
	 Turtle[] ts = turtlesHere();
	 if (ts != null)
		 for (int i=0; i < ts.length;i++)
			 if (ts[i].getColor() == Color.green)
				 sendMessage(ts[i].getAddress(),new VirusMessage());
	 return("red");
 }
}



/**Just a new Message class to identify them in Madkit
 (with the message Tracer agent for example)*/

 @SuppressWarnings("serial")
class VirusMessage extends Message {}
