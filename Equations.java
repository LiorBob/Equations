import java.applet.*;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.io.*;
import java.util.Vector;

import sun.audio.*;



public class Equations extends JFrame implements ActionListener,KeyListener
{
	private	AudioClip audioClip;
	private JTextField equationField;
	private JTextField resultField;
	
	private JButton solveButton;
	
	
	// Using Generics here ...  (feature of Java SE 1.5.0 and above)
	
	private Vector<String> soundsVector = new Vector<String>();

	
	private Timer timer;
	private final int DELAY = 1250;
	
	private int soundsIndex = 0;

	private String newSide;
	
	
	private boolean decimalPointFound;
	
	
	private final String LOADING_SOUNDS = "Loading sounds ... ";
	
	
	// Hebrew Unicode characters
	
	private final String ALEF = "\u05D0", BET = "\u05D1",
						GIMEL = "\u05D2" , DALET = "\u05D3",
						HE = "\u05D4", VAV = "\u05D5" ,
						ZAYEN = "\u05D6", HET = "\u05D7" ,
						TET = "\u05D8", YOD = "\u05D9",
						KAF = "\u05DB", LAMED = "\u05DC" ,
						MEM = "\u05DE", NUN = "\u05E0" ,
						SAMEKH = "\u05E1" , AYIN = "\u05E2",
						PE = "\u05E4" , TSADI = "\u05E6",
						QOF = "\u05E7" , RESH = "\u05E8",
						SHIN = "\u05E9", TAV = "\u05EA" ,
						FINALMEM = "\u05DD", FINALNUN = "\u05DF",
						FINALTSADI = "\u05E5" ;
	
	
	private final String NO_SOLUTION = 
		ALEF + YOD + FINALNUN + " " + PE + TAV + RESH + VAV + FINALNUN +
		" " + LAMED + MEM + SHIN + VAV + VAV + ALEF + HE + ".";
		 
    private final String EVERY_REAL = "." + MEM + MEM + SHIN + YOD 
       + " X " + PE + TAV + RESH + VAV + FINALNUN +
    	" " + HE + MEM + SHIN + VAV + VAV + ALEF + HE +
    	" " + HE + VAV + ALEF + " " + KAF + LAMED;
   
	
	public static void main(String[] args)
	{
		new Equations();		// call constructor  which is non-static method
	}


	

	public Equations()
	{
		equationField = new JTextField();
		resultField = new JTextField(20);
		
		solveButton = new JButton(PE + TAV + VAV + RESH + "!");
		
		solveButton.setBackground(new Color(0,200,0));
		solveButton.setForeground(Color.white);
		solveButton.setFont(new Font("Arial",Font.BOLD,16));
		
		solveButton.addActionListener(this);
		
		equationField.addKeyListener(this);
		
		
		timer = new Timer(DELAY,this);


		initializeUI();
		
	}
	
	
	public void keyPressed(KeyEvent e)
	{
		/* If equal sign (=)  was already found  in equation  and
		   =  sign was pressed , then disable typing =  sign . */
		   
		if (equationField.getText().indexOf("=") != -1)
		{
			if (e.getKeyChar() == '=')
			{
				equationField.setBackground(Color.white);				
				equationField.setEditable(false);
			}
				
			else
			{
				equationField.setEditable(true);
			}
				
		}
		
	}
	

	public void keyReleased(KeyEvent e)
	{
		
	}
	
	public void keyTyped(KeyEvent e)
	{

	}
	
	
	public void actionPerformed(ActionEvent event)
	{
		getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));


		// Disabling the controls on the applet while solving the equation .

		solveButton.setEnabled(false);
		equationField.setEnabled(false);

		
		decimalPointFound = false;
		
		
		if (! timer.isRunning())
		{
			resultField.setVisible(false);
			
			
			if (equationField.getText().indexOf("=") == -1)
			{
            	// Equal sign (=) was not found
            	JOptionPane.showMessageDialog(null, LAMED + ALEF + " "
            		+ MEM + VAV + PE + YOD + AYIN + " = " 
            	 	+ BET + MEM + SHIN + VAV + VAV + ALEF + HE);
	       	}

			else if ((equationField.getText().indexOf("x") == -1) &&
				(equationField.getText().indexOf("X") == -1))
			{
            	// x or X sign was not found
            	JOptionPane.showMessageDialog(null, LAMED + ALEF + " "
            		+ QOF + YOD + YOD + FINALMEM + " X "  
            	 	+ BET + MEM + SHIN + VAV + VAV + ALEF + HE);
	       	}

			else
			{
	    	    /* Left side is empty  (ie. the = sign is the leftmost
    	    	   character in the original equation) :  the left side
        	  		will be 0. */
			
				if (equationField.getText().indexOf("=") == 0)
				{
					equationField.setText("0" + equationField.getText());
				}


		        /* Right side is empty  (ie. the = sign is the rightmost
    	    		character in the original equation) :  the right side
        			will be 0. */

				if (equationField.getText().indexOf("=") == equationField.getText().length() - 1)
				{
					equationField.setText(equationField.getText() + "0");
				}
				
				solveEquation();
				
			}

			
			getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			
			// enabling the controls on the applet after solving the equation .
			
			solveButton.setEnabled(true);
			equationField.setEnabled(true);


			return;
			
		}
        

		if (soundsIndex < soundsVector.size())
		{
			String soundFile = soundsVector.get(soundsIndex);
			

			try
			{

	    			// create an audiostream from inputstream
				AudioStream audioStream = new AudioStream(new FileInputStream(soundFile));

			    	// play the audio clip with the audioplayer class
				AudioPlayer.player.start(audioStream);
			}

			catch (Exception e)
			{
				System.out.println("Error playing audio");
			}



			
			soundsIndex++;
			
			
			if (soundFile.equals("solution.au"))
			{
				timer.setDelay(1000);
			}
			else if (soundFile.equals("5.au"))
			{
				timer.setDelay(625);
			}
			else if (soundFile.equals("x.au"))
			{
				timer.setDelay(563);
			}
			else if (soundFile.equals("equals.au"))
			{
				timer.setDelay(375);
			}
			else if (soundFile.equals("0.au"))
			{
				timer.setDelay(188);
			}
			else if (soundFile.equals("is.au"))
			{
				timer.setDelay(375);
			}

		}
		
		else
		{
			soundsVector.clear();
			soundsIndex = 0;
			
			resultField.setVisible(true);
			
			getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
			
			// enabling the controls on the applet after solving the equation .
			
			solveButton.setEnabled(true);
			equationField.setEnabled(true);
			
			timer.stop();
			
		}
		
	}

	
	public void initializeUI()
	{
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridLayout(1,1));
		
		panel.add(equationField);
		panel.add(solveButton);
		panel.add(resultField);
		

		Container container = getContentPane();
		
		container.setLayout(new FlowLayout());
		container.add(panel);
		
		
		resultField.setVisible(false);
		

		JFrame frame = new JFrame("Equations");

	        frame.add(container);
	        frame.setSize(800, 600);
        	frame.setLocationRelativeTo(null);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.setVisible(true);



		try
		{
    			// create an audiostream from inputstream
			AudioStream audioStream = new AudioStream(new FileInputStream("EnterEquation.au"));

		    	// play the audio clip with the audioplayer class
			AudioPlayer.player.start(audioStream);
		}

		catch (Exception e)
		{
			System.out.println("Error playing audio");
		}

	}


	private void solveEquation()
	{
        String	 strOriginalEquation;
        String	 strLeftSide, strNewLeftSide;
        String	 strRightSide, strNewRightSide;
        String	 strEquationBeforeResult;
        String	 strFinalResult;
        int			intEqualSignPosition;

        strOriginalEquation = equationField.getText();
        intEqualSignPosition = strOriginalEquation.indexOf("=");

		strLeftSide = strOriginalEquation.substring(0,intEqualSignPosition);

        strRightSide = strOriginalEquation.substring(intEqualSignPosition + 1);

        strNewLeftSide = insertOnesPlus(strLeftSide);
        strNewRightSide = insertOnesPlus(strRightSide);
        
        strNewLeftSide = collectTerms(strNewLeftSide);
        strNewRightSide = collectTerms(strNewRightSide);
        
        strEquationBeforeResult = collectAllTerms(strNewLeftSide + "=" + strNewRightSide);

        strFinalResult = calculateResult(strEquationBeforeResult);

	   	showResult(strFinalResult);

	}
	
	private String insertOnesPlus(String strSide)
	{
        String	strNewSide = strSide;
        char	strCurrent, strNext;

        int		intCurrentPosition;


        // puts "1" in the beginning of the side ,
        // if x is the first character in that side.

        if ((strNewSide.charAt(0) == 'x') || (strNewSide.charAt(0) == 'X'))
		{        
        	strNewSide = "1" + strNewSide;
		}


        // if the combination "-x" or "+x" is found ,
        // then it is converted to "-1x" or "+1x" ,
        // respectively .

        for (intCurrentPosition = strNewSide.length() - 2; 
        		intCurrentPosition >=0 ; intCurrentPosition--)
		{
            strCurrent = strNewSide.charAt(intCurrentPosition);
            strNext = strNewSide.charAt(intCurrentPosition + 1);

            if (((strCurrent == '-') || (strCurrent == '+')) && 
                ((strNext == 'x') || (strNext == 'X')))
			{
                strNewSide = strNewSide.substring(0,intCurrentPosition + 1)
                					+ "1" + strNewSide.substring(intCurrentPosition + 1);
			}
			
		}


        // If the side begins with a digit , then
        // the side will start with "+" .

		if ((strNewSide.charAt(0)  >= '0') &&
			(strNewSide.charAt(0)  <= '9'))
		{
            strNewSide = "+" + strNewSide;
		}
		
        return strNewSide;		
	}
	
	
	private String collectTerms(String strSide) 
	{
    	String strNewSide = strSide;
    	String strTotalX;
        String strTotalFree;
        
		strTotalX = collectXTerms(strNewSide);
		strNewSide = newSide;
		strTotalFree = collectFreeTerms(strNewSide);
		

		if (strTotalFree.charAt(0) != '-')
		{
            strNewSide = strTotalX + "+" + strTotalFree;
		}

		else
		{
            strNewSide = strTotalX + strTotalFree;
		} 

        return strNewSide;
		
	}

	
	private String collectXTerms(String strSide) 
	{
        StringBuffer strNewSide = new StringBuffer().append(strSide);
        String strXCoefficient;
        String strTotalX;
		char	strCurrent;

        double dblSumXCoefficients = 0;

        int		intCurrentXPosition = 0;
        int		intCurrentPosition;
        
        
        /* Scans the side from right to left , in order
        	to find the X coefficients . 
        	dblSumXCoefficients holds the sum of all of
        	the X coefficients in the same side . */
        	
        for (intCurrentPosition = strNewSide.length() -1; 
        		intCurrentPosition >= 0; intCurrentPosition--)	
		{
            strCurrent = strNewSide.charAt(intCurrentPosition);

            if ((strCurrent == 'x') || (strCurrent == 'X'))
			{
                intCurrentXPosition = intCurrentPosition;
			}

            if (((strCurrent == '-') || (strCurrent == '+')) &&
                (intCurrentXPosition != 0))
			{
                strXCoefficient = strNewSide.substring(intCurrentPosition, intCurrentXPosition);
                
                dblSumXCoefficients +=  Double.parseDouble(strXCoefficient);

                /* The following line helps collecting the free coefficients .
                	It removes all the instances of X coefficients in the same side .
                	The start index for removing is intCurrentPosition - 1 , because
                	the Remove method works with 0-based arrays . */

              	strNewSide = strNewSide.delete(intCurrentPosition, intCurrentXPosition + 1);

                intCurrentXPosition = 0;

			}
			
		}        
        
        newSide = strNewSide.toString();
        
        strTotalX = dblSumXCoefficients + "x";
        
        return strTotalX;
		
	}


    private String collectFreeTerms(String strSide)
	{
    	String aStrFreeCoefficients[];

        String strNewSide = strSide;
	    String strTotalFree;
	    
        char strCurrent;

        double dblSumFreeCoefficients = 0;

        int		 intCurrentPosition;
        int		 intElementIndex;


        // Separates the free coefficients using the
        // space character .

        for (intCurrentPosition = strNewSide.length() - 1;
        		intCurrentPosition >= 0 ; intCurrentPosition--)
		{
            strCurrent = strNewSide.charAt(intCurrentPosition);

            if ((strCurrent == '+') || (strCurrent == '-'))
			{
                strNewSide = strNewSide.substring(0,intCurrentPosition)
                					+ " " + strNewSide.substring(intCurrentPosition);
			}

        }
        

      	// Builds a string array with the free coefficients .

		aStrFreeCoefficients = strNewSide.split(" ");
		
		
        // dblSumFreeCoefficients holds the sum of all of
        // the free coefficients in the same side .

        for (intElementIndex = 0; 
        		intElementIndex < aStrFreeCoefficients.length ;
        		intElementIndex++)
		{
			if (! (aStrFreeCoefficients[intElementIndex].equals("")))
			{
	        	dblSumFreeCoefficients += 
	        		Double.parseDouble(aStrFreeCoefficients[intElementIndex]);
	        }
		}
		

        strTotalFree = dblSumFreeCoefficients + "";
        
        return strTotalFree;

    }


    // Collects terms (X-terms and free terms ,
    // separately) from both sides of the equation .

    private String collectAllTerms(String strEquation)
	{
        String strTotalX;
        String strTotalFree;

        char strCurrent;

        int		 intEqualSignPosition;
        int		 intCurrentPosition;

        intEqualSignPosition = strEquation.indexOf("=");


        /*	Checks if the first character after equal sign
        	is a digit. If yes , its sign is needed to be "-" .
        	In the Else part : Character after equal sign
        	is "-" : needed to be replaced with "+" .
        	This is sign replacement in the right side . */

        if ((strEquation.charAt(intEqualSignPosition + 1) >= '0') &&
        	(strEquation.charAt(intEqualSignPosition + 1) <= '9'))
		{
             strEquation = strEquation.substring(0,intEqualSignPosition + 1)
                					+ "-" + strEquation.substring(intEqualSignPosition + 1);
		}
		
		else
		{
             strEquation = strEquation.substring(0,intEqualSignPosition + 1)
                					+ "+" + strEquation.substring(intEqualSignPosition + 2);
		}


        intCurrentPosition = intEqualSignPosition - 1;


        // The following loop locates the position of 
        // the sign to replace ("+" or "-") in the left side .

        do
		{
        	intCurrentPosition -= 1;
            strCurrent = strEquation.charAt(intCurrentPosition);
            
        } while ((strCurrent != '+') && (strCurrent != '-'));


        // The following code is sign replacement in the left side .

        if (strCurrent == '+')
		{
             strEquation = strEquation.substring(0,intCurrentPosition)
                					+ "-" + strEquation.substring(intCurrentPosition + 1);
		}
		
		else
		{
			
             strEquation = strEquation.substring(0,intCurrentPosition)
                					+ "+" + strEquation.substring(intCurrentPosition + 1);
		}
		

		StringBuffer equation = new StringBuffer().append(strEquation);

        //	Removes the "=" sign from the equation ,
        //	in order to work properly with the
        //	'terms-collecting functions.

		equation = equation.delete(intEqualSignPosition, intEqualSignPosition + 1);

		strEquation = equation.toString();


        // 	The following If statement is similar to 
        //	the "Plus" part in InsertOnesPlus function.

        if ((strEquation.charAt(0) >= '0') && (strEquation.charAt(0) <= '9'))
		{
            strEquation = "+" + strEquation;
        }


        strTotalX = collectXTerms(strEquation);
        strEquation =  newSide;
        strTotalFree = collectFreeTerms(strEquation);

        return strTotalX + "=" + strTotalFree;

    }


    private String calculateResult(String strEquation)
	{
        String strFreeCoefficient;
        String strXCoefficient;

        String strResult;

        double dblFreeCoefficient;
        double dblXCoefficient;

        double dblResult;

        int		 intEqualSignPosition;
        int		 intXPosition;


        intEqualSignPosition = strEquation.indexOf("=");
        intXPosition = strEquation.indexOf("x");

        strFreeCoefficient = strEquation.substring(intEqualSignPosition + 1);
        strXCoefficient = strEquation.substring(0, intXPosition);

        dblFreeCoefficient = Double.parseDouble(strFreeCoefficient);
        dblXCoefficient = Double.parseDouble(strXCoefficient);


        if (dblXCoefficient == 0.0)
		{
            if (dblFreeCoefficient != 0.0)
			{
                return NO_SOLUTION;
			}

			else
			{
                return EVERY_REAL;
			}
		}
		

        dblResult = dblFreeCoefficient / dblXCoefficient;
        

		if (String.valueOf(dblResult).endsWith(".0"))
		{
			if (dblResult == -0.0)
       		{
       			// solves bug when only 0 is on the left side of the equation, and
       			// the result is 0  (the result was written as "x=-0" instead of "0")
       			
       			strResult = "x=0";		
       		}
			
			else
			{
	            strResult = "x=" + 
    	        	String.valueOf(dblResult).substring(0,
        	    		String.valueOf(dblResult).indexOf("."));
       		}
       		
		}
		
        else
		{
            strResult = "x=" + dblResult;
        } 

        return strResult;

    }
    

    private void showResult(String strResult)
	{
        /* if (strResult.indexOf("=") == -1) is the case of
           NO_SOLUTION or EVERY_REAL   ("=" sign wasn't found in
           the result).
           if (strResult.indexOf("X") == -1) is the case of
           NO_SOLUTION . */
        
        if (strResult.indexOf("=") == -1)
		{
        	if (strResult.indexOf("X") == -1)
			{
                soundsVector.addElement("nosolution.au");
                
                translateEquation(equationField.getText());
                
				resultField.setVisible(true);
                resultField.setText(NO_SOLUTION);

			}
				
			else
			{
                soundsVector.addElement("solution.au");
               	translateEquation(equationField.getText());

                resultField.setVisible(true);
				resultField.setText(EVERY_REAL);
				
           		soundsVector.addElement("everyreal.au");
			}
			
		}
		
		else
		{
			soundsVector.addElement("solution.au");
			translateEquation(equationField.getText());

			
			soundsVector.addElement("is.au");
			
			resultField.setText(strResult);
            translateEquation(strResult);
        }

        getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }


	private void translateEquation(String equation)	
	{
    	String stringToSpeak = "" ;
    	char currentChar;

        int currentPosition = 0;
		
		solveButton.setFocusable(false);

		
        do 
		{
	    	currentChar = equation.charAt(currentPosition);

        	if  ((currentChar >= '0') && (currentChar <= '9'))
			{
                stringToSpeak += currentChar;
			
              	if (currentPosition == equation.length() - 1)
				{
	            	sayTheNumber(stringToSpeak);
                    stringToSpeak = "";
				}
			}

            else
			{
                if (! stringToSpeak.equals(""))
				{
	            	sayTheNumber(stringToSpeak);
                    stringToSpeak = "";
				}
				
                if (currentChar == '+')  
				{
					soundsVector.addElement("plus.au");
				}
				
				if (currentChar == '=')
				{
					soundsVector.addElement("equals.au");
				}
				
                if (currentChar == '-')
				{
                    if ((currentPosition == 0) ||  (equation.charAt(currentPosition - 1) == '=')) 
					{
						soundsVector.addElement("minus.au");
					}
					
                    else
					{
						soundsVector.addElement(currentChar + ".au");
					}
				}
				
                if  ((currentChar == 'x') ||  (currentChar == 'X'))
				{
					soundsVector.addElement("x.au");
				}

                if (currentChar == '*')
				{
					soundsVector.addElement("mul.au");
				}
				
                if (currentChar == '/')
				{
					soundsVector.addElement("div.au");
				}

                if (currentChar == '.')
				{
					soundsVector.addElement("point.au");

                    decimalPointFound = true;
                }

			}
			
            currentPosition++;

		}	while  (currentPosition < equation.length());
		
		timer.start();
		
	}
	
    private void sayTheNumber(String stringToSpeak)
	{
    	int intCounter;
        int intDigitPosition;
        char strDigit;
        String strDigitToSpeak;


    	if ((decimalPointFound) || (stringToSpeak.length() > 4)) 
		{
            for (intCounter = 0; intCounter < stringToSpeak.length(); intCounter++)
			{
                strDigit = stringToSpeak.charAt(intCounter);
                
                soundsVector.addElement(strDigit + ".au");
			}

            decimalPointFound = false;

            return;

		}


        if (Integer.parseInt(stringToSpeak) <= 20)
		{
			soundsVector.addElement(stringToSpeak + ".au");
			return;
		}


        for (intCounter = 0; intCounter < stringToSpeak.length(); intCounter++)
		{
            strDigit = stringToSpeak.charAt(intCounter);
            intDigitPosition = stringToSpeak.length() - intCounter;

            if ((strDigit == '1') && (intDigitPosition == 2))
			{
                String strTwoRightmostDigits;

                if ((stringToSpeak.endsWith("12")) ||
                	(stringToSpeak.endsWith("13")) ||
                	(stringToSpeak.endsWith("17")) ||
                	(stringToSpeak.endsWith("18")) ||
                	(stringToSpeak.endsWith("19")))
				{
                	soundsVector.addElement("u.au");
                }

              	else
				{
            	    soundsVector.addElement("and.au");
            	}

               	strTwoRightmostDigits = stringToSpeak.charAt(stringToSpeak.length() - 2)
                									  + "" + stringToSpeak.charAt(stringToSpeak.length() - 1);
                									  
                soundsVector.addElement(strTwoRightmostDigits + ".au");

                break;
                
            }

            strDigitToSpeak =  String.valueOf(Integer.parseInt(strDigit + "") 
            							*  (int)(Math.pow(10, intDigitPosition-1)));

            if (strDigit != '0')
            {
                if (intCounter == stringToSpeak.length() - 1)
				{
                    if ((strDigit != '2') && (strDigit != '8'))
					{
                        soundsVector.addElement("and.au");
					}
					
                    else
					{
                        soundsVector.addElement("u.au");
					}
                }

				soundsVector.addElement(strDigitToSpeak + ".au");
				
            }

        }

    }
	
}