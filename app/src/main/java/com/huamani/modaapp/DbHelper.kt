package com.huamani.modaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "modaapp.db"
        private const val DATABASE_VERSION = 1

        // Table names
        const val TABLE_USUARIO = "usuario"
        const val TABLE_CATEGORIA = "categoria"
        const val TABLE_ROPA = "ropa"

        // Usuario columns
        const val COL_USER_ID = "id"
        const val COL_USER_NAME = "usuario"
        const val COL_USER_CLAVE = "clave"
        const val COL_USER_ROL = "rol"
        const val COL_USER_TELEFONO = "telefono"

        // Categoria columns
        const val COL_CAT_ID = "id"
        const val COL_CAT_NOMBRE = "nombre"

        // Ropa columns
        const val COL_ROPA_ID = "id"
        const val COL_ROPA_MODELO = "modelo"
        const val COL_ROPA_CATEGORIA = "categoria"
        const val COL_ROPA_TALLA = "talla"
        const val COL_ROPA_MARCA = "marca"
        const val COL_ROPA_COLOR = "color"
        const val COL_ROPA_PRECIO = "precio"
        const val COL_ROPA_CANTIDAD = "cantidad"
        const val COL_ROPA_FOTO = "foto"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsuarioTable = """
            CREATE TABLE $TABLE_USUARIO (
                $COL_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_USER_NAME TEXT UNIQUE,
                $COL_USER_CLAVE TEXT,
                $COL_USER_ROL TEXT,
                $COL_USER_TELEFONO TEXT
            )
        """.trimIndent()

        val createCategoriaTable = """
            CREATE TABLE $TABLE_CATEGORIA (
                $COL_CAT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CAT_NOMBRE TEXT UNIQUE
            )
        """.trimIndent()

        val createRopaTable = """
            CREATE TABLE $TABLE_ROPA (
                $COL_ROPA_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ROPA_MODELO TEXT NOT NULL,
                $COL_ROPA_CATEGORIA TEXT,
                $COL_ROPA_TALLA TEXT,
                $COL_ROPA_MARCA TEXT,
                $COL_ROPA_COLOR TEXT,
                $COL_ROPA_PRECIO REAL CHECK($COL_ROPA_PRECIO > 0),
                $COL_ROPA_CANTIDAD INTEGER CHECK($COL_ROPA_CANTIDAD >= 0),
                $COL_ROPA_FOTO TEXT
            )
        """.trimIndent()

        db.execSQL(createUsuarioTable)
        db.execSQL(createCategoriaTable)
        db.execSQL(createRopaTable)

        // Insert seed data
        insertSeedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROPA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }

    private fun insertSeedData(db: SQLiteDatabase) {
        // Admin user (HU-04)
        val adminValues = ContentValues().apply {
            put(COL_USER_NAME, "admin")
            put(COL_USER_CLAVE, "1234")
            put(COL_USER_ROL, "ADMIN")
            put(COL_USER_TELEFONO, "987654321")
        }
        db.insert(TABLE_USUARIO, null, adminValues)

        // Categories
        val categories = listOf("Polos", "Pantalones", "Vestidos", "Casacas", "Zapatillas")
        for (cat in categories) {
            val cv = ContentValues().apply {
                put(COL_CAT_NOMBRE, cat)
            }
            db.insert(TABLE_CATEGORIA, null, cv)
        }

        // Initial Ropa seed data (HU-06)
        val seedRopa = listOf(
            ContentValues().apply {
                put(COL_ROPA_MODELO, "Polo oversize")
                put(COL_ROPA_CATEGORIA, "Polos")
                put(COL_ROPA_TALLA, "M")
                put(COL_ROPA_MARCA, "Urban")
                put(COL_ROPA_COLOR, "Azul")
                put(COL_ROPA_PRECIO, 39.90)
                put(COL_ROPA_CANTIDAD, 12)
                put(COL_ROPA_FOTO, "")
            },
            ContentValues().apply {
                put(COL_ROPA_MODELO, "Polo básico")
                put(COL_ROPA_CATEGORIA, "Polos")
                put(COL_ROPA_TALLA, "S")
                put(COL_ROPA_MARCA, "Basic")
                put(COL_ROPA_COLOR, "Negro")
                put(COL_ROPA_PRECIO, 29.90)
                put(COL_ROPA_CANTIDAD, 20)
                put(COL_ROPA_FOTO, "")
            },
            ContentValues().apply {
                put(COL_ROPA_MODELO, "Jean slim")
                put(COL_ROPA_CATEGORIA, "Pantalones")
                put(COL_ROPA_TALLA, "32")
                put(COL_ROPA_MARCA, "Levis")
                put(COL_ROPA_COLOR, "Gris")
                put(COL_ROPA_PRECIO, 89.90)
                put(COL_ROPA_CANTIDAD, 15)
                put(COL_ROPA_FOTO, "")
            },
            ContentValues().apply {
                put(COL_ROPA_MODELO, "Vestido midi")
                put(COL_ROPA_CATEGORIA, "Vestidos")
                put(COL_ROPA_TALLA, "S")
                put(COL_ROPA_MARCA, "Zara")
                put(COL_ROPA_COLOR, "Morado")
                put(COL_ROPA_PRECIO, 119.00)
                put(COL_ROPA_CANTIDAD, 8)
                put(COL_ROPA_FOTO, "")
            }
        )

        for (ropa in seedRopa) {
            db.insert(TABLE_ROPA, null, ropa)
        }
    }

    // --- CRUD Methods ---

    fun validarAdmin(user: String, pass: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_USUARIO,
            arrayOf(COL_USER_ID),
            "$COL_USER_NAME = ? AND $COL_USER_CLAVE = ? AND $COL_USER_ROL = ?",
            arrayOf(user, pass, "ADMIN"),
            null, null, null
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun insertarRopa(modelo: String, categoria: String, talla: String, marca: String, color: String, precio: Double, cantidad: Int, foto: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_ROPA_MODELO, modelo)
            put(COL_ROPA_CATEGORIA, categoria)
            put(COL_ROPA_TALLA, talla)
            put(COL_ROPA_MARCA, marca)
            put(COL_ROPA_COLOR, color)
            put(COL_ROPA_PRECIO, precio)
            put(COL_ROPA_CANTIDAD, cantidad)
            put(COL_ROPA_FOTO, foto)
        }
        return db.insert(TABLE_ROPA, null, values)
    }

    fun obtenerCategorias(): List<String> {
        val list = mutableListOf<String>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT $COL_CAT_NOMBRE FROM $TABLE_CATEGORIA", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun obtenerRopa(categoriaFiltro: String? = null): List<Product> {
        val list = mutableListOf<Product>()
        val db = this.readableDatabase
        val query: String
        val args: Array<String>?

        if (categoriaFiltro != null && categoriaFiltro != "Todas") {
            query = "SELECT * FROM $TABLE_ROPA WHERE $COL_ROPA_CATEGORIA = ?"
            args = arrayOf(categoriaFiltro)
        } else {
            query = "SELECT * FROM $TABLE_ROPA"
            args = null
        }

        val cursor = db.rawQuery(query, args)
        if (cursor.moveToFirst()) {
            do {
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_MODELO))
                val categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_CATEGORIA))
                val talla = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_TALLA))
                val color = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_COLOR))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ROPA_PRECIO))
                val foto = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_FOTO))

                val details = "Talla $talla - $color"
                list.add(Product(modelo, details, precio, categoria, foto))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
