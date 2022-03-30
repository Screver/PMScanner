package be.sremy.maraudeursscanner

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class EmpModelClass(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "tickets.db"
        private const val TBL_TICKET = "tickets"

        // table conts
        private const val ID = "_id"
        private const val DAY = "day"
        private const val FLAG = "flag"

        // populate const
        private const val NEWDAY = "Samedi"
        private const val NEWFLAG = "FALSE"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.execSQL("DROP TABLE tickets")

        // creating table with fields
        // CREATE TABLE tickets(_id INTEGER PRIMARY KEY, day TEXT, number TEXT, flag TEXT)
        val createTblTicket = ("CREATE TABLE " + TBL_TICKET + "(" + ID + " INTEGER PRIMARY KEY," +
                DAY + " TEXT," +
                FLAG + " TEXT" + ")")

        val populateTickets = ("INSERT INTO " + TBL_TICKET + "(" +DAY + " ," + FLAG + ") VALUES('" + NEWDAY + "' ," + NEWFLAG + ")")

        db?.execSQL(createTblTicket)

        for (i in 1..440) {
            db?.execSQL(populateTickets)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
}
