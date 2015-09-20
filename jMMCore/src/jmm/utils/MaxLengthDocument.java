/**
 * Copyright (c) 2010-2015 Bryan Beck.
 * All rights reserved.
 * 
 * This project is licensed under LGPL v2.1.
 * See jMovieManager-license.txt for details.
 * 
 */
package jmm.utils;

import java.util.regex.Pattern;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import jmm.interfaces.RegexInterface;
import static jmm.interfaces.RegexInterface.REGEX_COMPLETE_RATING;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

/**
 *
 * @author Bryan Beck
 * @since 17.09.2011
 * 
 */
public class MaxLengthDocument extends PlainDocument implements RegexInterface{
    /** Logger. */
    private static final Logger LOG = LoggerFactory.logger(MaxLengthDocument.class);
    
    //Store maximum characters permitted
    private final int maxChars;
    
    private final int option;
    private JTextField component;
    
    public static final int INTEGER = 0;
    public static final int DOUBLE = 1;
    public static final int STRING = 2;
    private final String regex;
    
    private final Pattern regex_complete_rating = Pattern.compile(REGEX_COMPLETE_RATING);
    
//    private FocusListener checkOnFocusLost = new FocusAdapter() {
//        
//        @Override
//        public void focusLost(FocusEvent e) {
//            //check if the entire string machtes regex
//            if(getOption() == DOUBLE){
//                String entireValue = component.getText();
//                if(!entireValue.matches(REGEX_COMPLETE_RATING)){
//                   
//                    component.setText(entireValue.split(REGEX_COMPLETE_RATING)[0]);
//                    Matcher matcher = regex_complete_rating.matcher(entireValue);
//                    if(matcher.matches()){
//                        System.out.println(matcher.group());
//                    }
//                }
//            }
//        }
//    };
    
    public MaxLengthDocument(int maxChars, int option){
        this.maxChars = maxChars;
        this.option = option;
        if(option == INTEGER){
            regex = REGEX_DIGITS_ONLY;
        }
        else if(option == DOUBLE){
            regex = REGEX_RATING;
        }
        else{
            regex = "";
        }       
    }

//    /**
//     * Adds an additional focus lost listener to check another valid regular expression 
//     * @param maxChars
//     * @param option
//     * @param component The component to add the focus lost listener
//     */
//    public MaxLengthDocument(int maxChars, int option, JTextField component){
//        this(maxChars, option);
//        this.component = component;
////        this.component.addFocusListener(checkOnFocusLost);
//    }
    
    /**
     * @return the maxChars
     */
    private int getMaxChars() {
        return maxChars;
    }

    /**
     * @return The option
     */
    private int getOption(){
        return option;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if(str != null && (getLength() + str.length() <= getMaxChars())){
            if((regex.isEmpty()) || ((!str.matches(regex)))){
                super.insertString(offs, str, a);
            }
        }
    }
}
