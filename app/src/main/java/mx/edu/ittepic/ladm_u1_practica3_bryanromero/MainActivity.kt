package mx.edu.ittepic.ladm_u1_practica3_bryanromero

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {

    var datos = ""
    var vector: Array<Int> = Array(10) { 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permisos()

        asignar.setOnClickListener {
            if (validar()){return@setOnClickListener}
            asignarValor()
        }//fin asignar

        mostrar.setOnClickListener {
            datos = ""
            (0..9).forEach {
                datos = datos + vector[it] + ","
            }

            mensaje(datos)
            resultado.text = datos
        }//fin mostrar

        guardar.setOnClickListener {
            if (noSD()) {
                mensaje("Inserte una memoria SD")
                return@setOnClickListener
            }//if
            if (nombreA.text.toString().isEmpty()) {
                mensaje("Ingrese nombre del archivo")
                return@setOnClickListener
            }//if

            try {
                //permisos() //comprobar y solicitar permisos SD
                var rutaSD = Environment.getExternalStorageDirectory()
                var datos = File(rutaSD.absolutePath, nombreA.text.toString() + ".txt")
                var salida = OutputStreamWriter(FileOutputStream(datos))
                var r = ""
                (0..9).forEach {
                    r = r + vector[it] + ","
                }
                salida.write(r)
                salida.flush()
                salida.close()
                mensaje("Archivo guardado")

                nombreA.setText("")

            } catch (error: IOException) {
                mensaje(error.message.toString())
            }
        }//fin guardar

        leer.setOnClickListener {
            if (noSD()) {
                mensaje("Inserte una memoria SD")
                return@setOnClickListener
            }
            if (leerA.text.toString().isEmpty()) {
                mensaje("Ingrese nombre del archivo")
                return@setOnClickListener
            }

            try {
                var rutaSD = Environment.getExternalStorageDirectory()
                var datos = File(rutaSD.absolutePath, leerA.text.toString() + ".txt")
                var entrada = BufferedReader(InputStreamReader(FileInputStream(datos)))

                var data = entrada.readLine()

                resultado.setText(data)

                var datos2 = data.split(",")

                (0..9).forEach {
                    vector[it] = datos2[it].toInt()
                }
                leerA.setText("")

            } catch (error: IOException) {
                mensaje(error.message.toString())
            }
        }//fin leer
    }


    fun permisos() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE), 0)
        } else {
            mensaje("Permisos ya otorgados")
        }
    }//fin permisos

    fun validar():Boolean{
        if (posicion.text.toString().toInt() < 0 || posicion.text.toString().toInt() > 9 || valor.text.toString().isEmpty() || posicion.text.toString().isEmpty()) {
            mensaje("Posición no valida")
            return true
        }
        return false
    }

    fun asignarValor() {
        var pos = posicion.text.toString().toInt()
        var va = valor.text.toString().toInt()

        vector[pos] = va
        valor.setText("")
        posicion.setText("")
    }//fin asignarValor


    private fun mensaje(m: String) {
        AlertDialog.Builder(this)
            .setTitle("Atención")
            .setMessage(m)
            .setPositiveButton("Aceptar") { d, i -> }
            .show()
    }//fin mensaje

    fun noSD(): Boolean {
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED) {
            return true
        }
        return false
    }//fin noSD

}//fin MainActivity
