package com.example.practicethread2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.practicethread2.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var started = false// 타미어가 실행중임을 알려주기 위한 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            start()
        }
        binding.btnPause.setOnClickListener {
            pause()
        }
        binding.btnStop.setOnClickListener {
            stop()
        }
    }

    val SET_TIME = 51
    val RESET = 52

    val handler = object : Handler(){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                SET_TIME -> {
                    val total = msg.arg1 //val total = msg.what : sendemptymessage를 통해 전달된 값을 what을 통해 꺼내 쓸 수 있음. (what이 그 용도로 제공되는 건 아닌데 암튼 그렇게 쓸 수 있음. what은 명령어를 구분하기 위해 사용하는 용도)
                    binding.textTimer.text = formatTime(total)

                }
                RESET -> {
                    binding.textTimer.text = "00:00"
                }

            }

        }
    }

    fun start() {
        started = true
        // sub thread (서브 스레드에선 ui에 직접 접근할 수 없음)
        thread(start = true) { // start : 쓰레드에 시작하라고 명령을 하는 것. thread가 가지고 있는 start라는 파라미터에 true세팅한 것
            var total = 0 // 전체 시간을 저장하는 변수
            while (true) {
                Thread.sleep(1000) //1초마다 실행
                if (!started) break
                total += 1
                val msg = Message()
                msg.what = SET_TIME // 위처럼 msg를 직접세팅하면 what에도 쓸 수 있고 arg1...등 세팅할 수 있음 -> 명령어가 여러개 있을 때 사용
                msg.arg1 = total
                handler.sendMessage(msg) // msg 오브젝트를 전달. sendEmptyMessage를 하면 what에만 값이 세팅되서 보내는거고 아예 메세지 객체를 직접 만들면 what에도 세팅할 수 있고 arg1에도 세팅할 수 있음
                //handler.sendEmptyMessage(total) // 핸들러에 total 값을 전달
            }
        }
    }

    fun pause() {
        started = false
    }

    fun stop() {
        started = false
        binding.textTimer.text = "00:00"
    }

    fun formatTime(time:Int) : String {
        val minute = String.format("%02d", time/60) // total을 60으로 나눠서 분을 구한다음에 두자리 숫자로 반환함
        val second = String.format("%02d", time%60)
        return "$minute:$second"
    }
}