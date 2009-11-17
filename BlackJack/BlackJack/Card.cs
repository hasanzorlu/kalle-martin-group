﻿//  OOP1 Semester Project 1
//  BlackJack console appliaction 
//  Kalle Grafstöm and Martin Moghadam 
//
//  This class contains the Card
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace BlackJack
{
    class Card
    {
        private CardType cardType;
        private Suits cardSuit;
        private int Id;
        private int Value;

        //-----------------------------------------------
        // Card from Ace to King
        //-----------------------------------------------
        public enum CardType
        {
            Ace = 0,
            Two = 1,
            Three = 2,
            Four = 3,
            Five = 4,
            Six = 5,
            Seven = 6,
            Eight = 7,
            Nine = 8,
            Ten = 9,
            Jack = 10,
            Queen = 11,
            King = 12
        }

        //-----------------------------------------------
        // The different suits
        //-----------------------------------------------
        public enum Suits
        {
            Clubs = 0,
            Diamonds = 1,
            Hearts = 2,
            Spades = 3
        }

        //-----------------------------------------------
        // Constructor sets the card value and id
        //-----------------------------------------------
        public Card(CardType type, Suits suit)
        {
            cardSuit = suit;
            cardType = type;
            Id = ((int)suit * 13) + (int)cardType + 1;
            Value = (int)cardType;
            if (Value > 9)
                Value = 9;
        }

        //-----------------------------------------------
        // Get methods
        //-----------------------------------------------
        public int CardID
        {
            get { return Id; }
        }

        public int CardValue
        {
            get { return Value; }
        }

        public CardType Face
        {
            get { return cardType; }
        }

        public Suits Suit
        {
            get { return cardSuit; }
        }

        //-----------------------------------------------
        // ToString override used to write the card name
        //      eg. "♠ Ace" 
        //-----------------------------------------------
        public override string ToString()
        {
            string tmp = "exec";
            if (cardSuit == Suits.Clubs)
            {
                tmp = "♣ ";
            }
            else if (cardSuit == Suits.Diamonds)
            {
                tmp = "♦ ";
            }
            else if (cardSuit == Suits.Hearts)
            {
                tmp = "♥ ";
            }
            else if (cardSuit == Suits.Spades)
            {
                tmp = "♠ ";
            }
            return (tmp + cardType.ToString());
        }

    }
}
