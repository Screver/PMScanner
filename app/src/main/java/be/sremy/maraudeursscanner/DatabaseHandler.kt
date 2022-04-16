package be.sremy.maraudeursscanner

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "tickets.db"
        private const val TBL_TICKET = "tickets"

        // table const
        private const val ID = "_id"
        private const val DAY = "day"
        private const val FLAG = "flag"

        // populate const

        //TODO Selon le jour de réprésentation
//        private const val NEWDAY = "Samedi"
        private const val NEWDAY = "Dimanche"

        private const val NEWFLAG = "FALSE"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTblTicket = ("CREATE TABLE " + TBL_TICKET + "(" + ID + " INTEGER PRIMARY KEY," +
                DAY + " TEXT," +
                FLAG + " TEXT" + ")")

        val populateTickets = ("INSERT INTO $TBL_TICKET($DAY ,$FLAG) VALUES('$NEWDAY' ,'$NEWFLAG')")

        db?.execSQL(createTblTicket)

        for (i in 1..440) {
            db?.execSQL(populateTickets)
        }

////        //TODO Selon les places à exclure
//        for (i in 43..46) {
//            db?.execSQL(deleteFraud(i))
//        }
//        for (i in 109..110) {
//            db?.execSQL(deleteFraud(i))
//        }
//        for (i in 79..81)
//            db?.execSQL(deleteFraud(i))
    }

////    //TODO Selon les places à exclure
//    fun deleteFraud (toDeleteId : Int): String {
//        return "DELETE FROM $TBL_TICKET WHERE $ID is $toDeleteId"
//    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun searchSingleTicket(id: Int): TicketModelClass {
        var ticket = TicketModelClass(id,"","")

        val db = this.readableDatabase
        var cursor: Cursor? = null

        val selectQuery = "SELECT * FROM $TBL_TICKET WHERE $ID = ${ticket.id}"

        var id: Int
        var day: String
        var flag : String

        cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                day = cursor.getString(cursor.getColumnIndexOrThrow(DAY))
                flag = cursor.getString(cursor.getColumnIndexOrThrow(FLAG))

            ticket =  TicketModelClass(id = id, day = day, flag = flag)
            } while (cursor.moveToNext())
        }
        cursor.close() // Added by myself, need to check if works
        return ticket

    }

    fun viewTickets(): ArrayList<TicketModelClass> {
        val ticketList: ArrayList<TicketModelClass> = ArrayList<TicketModelClass>()

        val selectQuery = "SELECT * FROM $TBL_TICKET"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var day: String
        var flag : String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID))
                day = cursor.getString(cursor.getColumnIndexOrThrow(DAY))
                flag = cursor.getString(cursor.getColumnIndexOrThrow(FLAG))

                val ticket =  TicketModelClass(id = id, day = day, flag = flag)
                ticketList.add(ticket)
            } while (cursor.moveToNext())
        }
        cursor.close() // Added by myself, need to check if works
        return ticketList
    }

    fun updateTicket(ticket : TicketModelClass) : Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(FLAG, ticket.flag)

        val success = db.update(TBL_TICKET, contentValues, ID + "=" + ticket.id, null)
        db.close()
        return success
    }

    fun countPresentTicket(): Int {
        var nmbre_tickets = 0

        val selectQuery = "SELECT DISTINCT * FROM $TBL_TICKET WHERE $FLAG = 'TRUE'"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        cursor = db.rawQuery(selectQuery, null)

        nmbre_tickets = cursor.count

        cursor.close()

        return nmbre_tickets
    }

}
