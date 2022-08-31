package com.example.practicethread2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.example.practicethread2.databinding.ActivityMainBinding
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    var total = 0 // 전체 시간을 저장하는 변수
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



    fun start() {
        started = true
        // sub thread (서브 스레드에선 ui에 직접 접근할 수 없음)
        thread(start = true) { // start : 쓰레드에 시작하라고 명령을 하는 것. thread가 가지고 있는 start라는 파라미터에 true세팅한 것
            while (true) {
                Thread.sleep(1000) //1초마다 실행
                if (!started) break
                total += 1
                //runOnUiThread : 핸들러 말고 서브 쓰레드에 접근할 수 있는 방법
                //runOnUiThread가 내부적으로 핸들러를 구현하고 있다고 생각하면 됨
                runOnUiThread {
                    binding.textTimer.text = formatTime(total)
                }
            }
        }
    }

    fun pause() {
        started = false
    }

    fun stop() {
        started = false
        total = 0
        binding.textTimer.text = "00:00"
    }

    fun formatTime(time:Int) : String {
        val minute = String.format("%02d", time/60) // total을 60으로 나눠서 분을 구한다음에 두자리 숫자로 반환함
        val second = String.format("%02d", time%60)
        return "$minute:$second"
    }
}