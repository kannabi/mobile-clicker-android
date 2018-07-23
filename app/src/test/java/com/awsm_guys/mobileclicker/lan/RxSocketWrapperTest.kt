package com.awsm_guys.mobileclicker.lan

import com.awsm_guys.mobileclicker.clicker.model.controller.lan.RxSocketWrapper
import com.awsm_guys.mobileclicker.utils.makeSingle
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.junit.Test
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.CountDownLatch

class RxSocketWrapperTest {

    @Test
    fun testBigString() {
        val countDownLatch = CountDownLatch(1)
        makeSingle(CompositeDisposable()) { imitateBigStringDesktop(countDownLatch) }

        val rxSocketWrapper = RxSocketWrapper(
            Socket().apply {
                connect(
                    InetSocketAddress("127.0.0.1", 8841), 5000
                )
            }, 512
        )

        rxSocketWrapper.inputObservable
                .subscribeOn(Schedulers.io())
                .subscribe(object : Observer<String> {
                    private var counter = 0

                    override fun onNext(data: String) {
                        when (counter) {
                            0 -> assert(data == smallString)
                            1 -> assert(data == bigString)
                        }
                        counter++
                        if (counter == 2) {
                            countDownLatch.countDown()
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        countDownLatch.countDown()
                    }

                    override fun onComplete() {}

                    override fun onSubscribe(d: Disposable) {}
                })

        countDownLatch.await()
        rxSocketWrapper.close()
    }

    private fun imitateBigStringDesktop(countDownLatch: CountDownLatch) {
        val rxSocketWrapper: RxSocketWrapper =
                ServerSocket().use {
                    it.reuseAddress = true
                    it.bind(InetSocketAddress(8841))
                    RxSocketWrapper(it.accept())
                }

        Thread.sleep(500)
        rxSocketWrapper.sendData(smallString)
        rxSocketWrapper.sendData(bigString)

        countDownLatch.await()
    }

    private val smallString = "Post hoc, ergo propter hoc"

    private val bigString =
            "From fairest creatures we desire increase,\n" +
                    "That thereby beauty's rose might never die,\n" +
                    "But as the riper should by time decease,\n" +
                    "His tender heir might bear his memory:\n" +
                    "But thou, contracted to thine own bright eyes,\n" +
                    "Feed'st thy light's flame with self-substantial fuel,\n" +
                    "Making a famine where abundance lies,\n" +
                    "Thyself thy foe, to thy sweet self too cruel.\n" +
                    "Thou that art now the world's fresh ornament\n" +
                    "And only herald to the gaudy spring,\n" +
                    "Within thine own bud buriest thy content\n" +
                    "And, tender churl, makest waste in niggarding.\n" +
                    "    Pity the world, or else this glutton be,\n" +
                    "    To eat the world's due, by the grave and thee." +
                    "\n\n" +
                    "When forty winters shall beseige thy brow,\n" +
                    "And dig deep trenches in thy beauty's field,\n" +
                    "Thy youth's proud livery, so gazed on now,\n" +
                    "Will be a tatter'd weed, of small worth held:\n" +
                    "Then being ask'd where all thy beauty lies, \n" +
                    "Where all the treasure of thy lusty days,\n" +
                    "To say, within thine own deep-sunken eyes,\n" +
                    "Were an all-eating shame and thriftless praise.\n" +
                    "How much more praise deserved thy beauty's use,\n" +
                    "If thou couldst answer 'This fair child of mine\n" +
                    "Shall sum my count and make my old excuse,'\n" +
                    "Proving his beauty by succession thine!\n" +
                    "    This were to be new made when thou art old,\n" +
                    "    And see thy blood warm when thou feel'st it cold. " +
                    "\n\n" +
                    "Look in thy glass, and tell the face thou viewest\n" +
                    "Now is the time that face should form another;\n" +
                    "Whose fresh repair if now thou not renewest,\n" +
                    "Thou dost beguile the world, unbless some mother.\n" +
                    "For where is she so fair whose unear'd womb 5\n" +
                    "Disdains the tillage of thy husbandry?\n" +
                    "Or who is he so fond will be the tomb\n" +
                    "Of his self-love, to stop posterity?\n" +
                    "Thou art thy mother's glass, and she in thee\n" +
                    "Calls back the lovely April of her prime: 10\n" +
                    "So thou through windows of thine age shall see\n" +
                    "Despite of wrinkles this thy golden time.\n" +
                    "    But if thou live, remember'd not to be,\n" +
                    "    Die single, and thine image dies with thee. " +
                    "\n\n" +
                    "Unthrifty loveliness, why dost thou spend\n" +
                    "Upon thyself thy beauty's legacy?\n" +
                    "Nature's bequest gives nothing but doth lend,\n" +
                    "And being frank she lends to those are free.\n" +
                    "Then, beauteous niggard, why dost thou abuse\n" +
                    "The bounteous largess given thee to give?\n" +
                    "Profitless usurer, why dost thou use\n" +
                    "So great a sum of sums, yet canst not live?\n" +
                    "For having traffic with thyself alone,\n" +
                    "Thou of thyself thy sweet self dost deceive.\n" +
                    "Then how, when nature calls thee to be gone,\n" +
                    "What acceptable audit canst thou leave?\n" +
                    "    Thy unused beauty must be tomb'd with thee,\n" +
                    "    Which, used, lives th' executor to be. " +
                    "\n\n" +
                    "Those hours, that with gentle work did frame\n" +
                    "The lovely gaze where every eye doth dwell,\n" +
                    "Will play the tyrants to the very same\n" +
                    "And that unfair which fairly doth excel:\n" +
                    "For never-resting time leads summer on \n" +
                    "To hideous winter and confounds him there;\n" +
                    "Sap cheque'd with frost and lusty leaves quite gone,\n" +
                    "Beauty o'ersnow'd and bareness every where:\n" +
                    "Then, were not summer's distillation left,\n" +
                    "A liquid prisoner pent in walls of glass, \n" +
                    "Beauty's effect with beauty were bereft,\n" +
                    "Nor it nor no remembrance what it was:\n" +
                    "    But flowers distill'd though they with winter meet,\n" +
                    "    Leese but their show; their substance still lives sweet. " +
                    "\n\n" +
                    "Then let not winter's ragged hand deface\n" +
                    "In thee thy summer, ere thou be distill'd:\n" +
                    "Make sweet some vial; treasure thou some place\n" +
                    "With beauty's treasure, ere it be self-kill'd.\n" +
                    "That use is not forbidden usury,\n" +
                    "Which happies those that pay the willing loan;\n" +
                    "That's for thyself to breed another thee,\n" +
                    "Or ten times happier, be it ten for one;\n" +
                    "Ten times thyself were happier than thou art,\n" +
                    "If ten of thine ten times refigured thee:\n" +
                    "Then what could death do, if thou shouldst depart,\n" +
                    "Leaving thee living in posterity?\n" +
                    "    Be not self-will'd, for thou art much too fair\n" +
                    "    To be death's conquest and make worms thine heir. }"
}