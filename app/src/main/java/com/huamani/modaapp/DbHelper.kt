package com.huamani.modaapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "modaapp.db"
        private const val DATABASE_VERSION = 2

        // Table names
        const val TABLE_USUARIO = "usuario"
        const val TABLE_CATEGORIA = "categoria"
        const val TABLE_ROPA = "ropa"
        const val TABLE_CLIENTE = "cliente"
        const val TABLE_PEDIDO = "pedido"
        const val TABLE_DETALLE_PEDIDO = "detalle_pedido"

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

        // Cliente columns
        const val COL_CLI_ID = "id"
        const val COL_CLI_TELEFONO = "telefono"
        const val COL_CLI_NOMBRES = "nombres"
        const val COL_CLI_APELLIDOS = "apellidos"
        const val COL_CLI_FECHA = "fecha_registro"

        // Pedido columns
        const val COL_PED_ID = "id"
        const val COL_PED_CLIENTE_ID = "cliente_id"
        const val COL_PED_FECHA = "fecha"
        const val COL_PED_TOTAL = "total"
        const val COL_PED_ESTADO = "estado"
        const val COL_PED_FECHA_ATENCION = "fecha_atencion"

        // Detalle Pedido columns
        const val COL_DET_ID = "id"
        const val COL_DET_PEDIDO_ID = "pedido_id"
        const val COL_DET_ROPA_ID = "ropa_id"
        const val COL_DET_CANTIDAD = "cantidad"
        const val COL_DET_PRECIO_UNIT = "precio_unitario"
        const val COL_DET_SUBTOTAL = "subtotal"
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

        val createClienteTable = """
            CREATE TABLE $TABLE_CLIENTE (
                $COL_CLI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CLI_TELEFONO TEXT UNIQUE,
                $COL_CLI_NOMBRES TEXT,
                $COL_CLI_APELLIDOS TEXT,
                $COL_CLI_FECHA TEXT
            )
        """.trimIndent()

        val createPedidoTable = """
            CREATE TABLE $TABLE_PEDIDO (
                $COL_PED_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PED_CLIENTE_ID INTEGER,
                $COL_PED_FECHA TEXT,
                $COL_PED_TOTAL REAL,
                $COL_PED_ESTADO TEXT,
                $COL_PED_FECHA_ATENCION TEXT,
                FOREIGN KEY($COL_PED_CLIENTE_ID) REFERENCES $TABLE_CLIENTE($COL_CLI_ID)
            )
        """.trimIndent()

        val createDetalleTable = """
            CREATE TABLE $TABLE_DETALLE_PEDIDO (
                $COL_DET_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_DET_PEDIDO_ID INTEGER,
                $COL_DET_ROPA_ID INTEGER,
                $COL_DET_CANTIDAD INTEGER,
                $COL_DET_PRECIO_UNIT REAL,
                $COL_DET_SUBTOTAL REAL,
                FOREIGN KEY($COL_DET_PEDIDO_ID) REFERENCES $TABLE_PEDIDO($COL_PED_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db.execSQL(createUsuarioTable)
        db.execSQL(createCategoriaTable)
        db.execSQL(createRopaTable)
        db.execSQL(createClienteTable)
        db.execSQL(createPedidoTable)
        db.execSQL(createDetalleTable)

        insertSeedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DETALLE_PEDIDO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PEDIDO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ROPA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIO")
        onCreate(db)
    }

    private fun insertSeedData(db: SQLiteDatabase) {
        val adminValues = ContentValues().apply {
            put(COL_USER_NAME, "admin")
            put(COL_USER_CLAVE, "1234")
            put(COL_USER_ROL, "ADMIN")
            put(COL_USER_TELEFONO, "987654321")
        }
        db.insert(TABLE_USUARIO, null, adminValues)

        val categories = listOf("Polos", "Pantalones", "Vestidos", "Casacas", "Zapatillas")
        for (cat in categories) {
            val cv = ContentValues().apply {
                put(COL_CAT_NOMBRE, cat)
            }
            db.insert(TABLE_CATEGORIA, null, cv)
        }

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

    // --- Sprint 3: Clientes y Pedidos ---

    fun buscarClientePorTelefono(telefono: String): Map<String, String>? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_CLIENTE,
            arrayOf(COL_CLI_NOMBRES, COL_CLI_APELLIDOS),
            "$COL_CLI_TELEFONO = ?",
            arrayOf(telefono),
            null, null, null
        )
        var result: Map<String, String>? = null
        if (cursor.moveToFirst()) {
            val nombres = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLI_NOMBRES))
            val apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLI_APELLIDOS))
            result = mapOf("nombres" to nombres, "apellidos" to apellidos)
        }
        cursor.close()
        return result
    }

    fun registrarPedidoCompleto(telefono: String, nombres: String, apellidos: String, total: Double, items: List<CartItem>): Long {
        val db = this.writableDatabase
        db.beginTransaction()
        try {
            // 1. Check or insert client
            var clienteId: Long = -1
            val cursor = db.query(TABLE_CLIENTE, arrayOf(COL_CLI_ID), "$COL_CLI_TELEFONO = ?", arrayOf(telefono), null, null, null)
            if (cursor.moveToFirst()) {
                clienteId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CLI_ID))
            }
            cursor.close()

            if (clienteId == -1L) {
                val fechaReg = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                val clientValues = ContentValues().apply {
                    put(COL_CLI_TELEFONO, telefono)
                    put(COL_CLI_NOMBRES, nombres)
                    put(COL_CLI_APELLIDOS, apellidos)
                    put(COL_CLI_FECHA, fechaReg)
                }
                clienteId = db.insert(TABLE_CLIENTE, null, clientValues)
            }

            // 2. Insert Pedido
            val fechaPed = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
            val pedidoValues = ContentValues().apply {
                put(COL_PED_CLIENTE_ID, clienteId)
                put(COL_PED_FECHA, fechaPed)
                put(COL_PED_TOTAL, total)
                put(COL_PED_ESTADO, "PENDIENTE")
                put(COL_PED_FECHA_ATENCION, "")
            }
            val pedidoId = db.insert(TABLE_PEDIDO, null, pedidoValues)

            // 3. Insert Detalle Pedido
            for (item in items) {
                val detalleValues = ContentValues().apply {
                    put(COL_DET_PEDIDO_ID, pedidoId)
                    put(COL_DET_ROPA_ID, 1) // default or matched ID
                    put(COL_DET_CANTIDAD, item.quantity)
                    put(COL_DET_PRECIO_UNIT, item.price)
                    put(COL_DET_SUBTOTAL, item.subtotal)
                }
                db.insert(TABLE_DETALLE_PEDIDO, null, detalleValues)
            }

            db.setTransactionSuccessful()
            return pedidoId
        } catch (e: Exception) {
            return -1L
        } finally {
            db.endTransaction()
        }
    }

    data class AdminProduct(
        val id: Int,
        val modelo: String,
        val categoria: String,
        val talla: String,
        val marca: String,
        val color: String,
        val precio: Double,
        val cantidad: Int,
        val foto: String
    )

    fun obtenerTodasLasPrendas(busqueda: String = ""): List<AdminProduct> {
        val list = mutableListOf<AdminProduct>()
        val db = this.readableDatabase
        val query: String
        val args: Array<String>?

        if (busqueda.isNotEmpty()) {
            query = "SELECT * FROM $TABLE_ROPA WHERE $COL_ROPA_MODELO LIKE ? OR $COL_ROPA_MARCA LIKE ?"
            args = arrayOf("%$busqueda%", "%$busqueda%")
        } else {
            query = "SELECT * FROM $TABLE_ROPA"
            args = null
        }

        val cursor = db.rawQuery(query, args)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROPA_ID))
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_MODELO))
                val categoria = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_CATEGORIA))
                val talla = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_TALLA))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_MARCA))
                val color = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_COLOR))
                val precio = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_ROPA_PRECIO))
                val cantidad = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ROPA_CANTIDAD))
                val foto = cursor.getString(cursor.getColumnIndexOrThrow(COL_ROPA_FOTO))

                list.add(AdminProduct(id, modelo, categoria, talla, marca, color, precio, cantidad, foto))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun eliminarPrenda(id: Int): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_ROPA, "$COL_ROPA_ID = ?", arrayOf(id.toString()))
    }
    data class Pedido(
        val id: Int,
        val clienteNombres: String,
        val clienteTelefono: String,
        val fecha: String,
        val total: Double,
        val estado: String
    )

    fun obtenerPedidos(estadoFiltro: String): List<Pedido> {
        val list = mutableListOf<Pedido>()
        val db = this.readableDatabase
        val query = "SELECT p.$COL_PED_ID, c.$COL_CLI_NOMBRES || ' ' || c.$COL_CLI_APELLIDOS as clienteNombres, c.$COL_CLI_TELEFONO as clienteTelefono, p.$COL_PED_FECHA, p.$COL_PED_TOTAL, p.$COL_PED_ESTADO FROM $TABLE_PEDIDO p JOIN $TABLE_CLIENTE c ON p.$COL_PED_CLIENTE_ID = c.$COL_CLI_ID WHERE p.$COL_PED_ESTADO = ? ORDER BY p.$COL_PED_ID DESC"
        val cursor = db.rawQuery(query, arrayOf(estadoFiltro))
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Pedido(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getString(5)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun actualizarEstadoPedido(pedidoId: Int, nuevoEstado: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_PED_ESTADO, nuevoEstado)
        }
        db.update(TABLE_PEDIDO, values, "$COL_PED_ID = ?", arrayOf(pedidoId.toString()))
    }
}