package com.smile.qzclould.ui.cloud.viewmodel

import android.arch.lifecycle.MediatorLiveData
import com.smile.qielive.common.mvvm.BaseViewModel
import com.smile.qielive.common.mvvm.ErrorStatus
import com.smile.qzclould.repository.HttpRepository
import com.smile.qzclould.db.Direcotory
import com.smile.qzclould.ui.cloud.bean.ParseUrlResultBean
import org.jetbrains.anko.doAsync

class CloudViewModel : BaseViewModel() {
    val repo by lazy { HttpRepository() }
    val createDirectoryResult by lazy { MediatorLiveData<Direcotory>() }
    val listFileResult by lazy { MediatorLiveData<List<Direcotory>>() }
    val parseUrlResult by lazy { MediatorLiveData<ParseUrlResultBean>() }
    val removeResult by lazy { MediatorLiveData<String>() }
    val errorStatus by lazy { MediatorLiveData<ErrorStatus>() }

    fun createDirectory(name: String, parentUUid: String = "") {
        repo.createDirectory(name, parentUUid)
                .subscribe({
                    if (it.success) {
                        createDirectoryResult.value = it.data
                    } else {
                        errorStatus.value = ErrorStatus(it.status, it.message)
                    }
                }, {
                    errorStatus.value = ErrorStatus(100, it.message)
                })
                .autoDispose()
    }

    fun listFile(parent: String, path: String, start: Int, size: Int, recycle: Int, mime: String, orderBy: Int, type: Int) {
        repo.listFile(parent, path, start, size, recycle, mime, orderBy, type)
                .subscribe({
                    if (it.success) {
                        listFileResult.value = it.data
                    } else {
                        errorStatus.value = ErrorStatus(it.status, it.message)
                    }
                }, {
                    errorStatus.value = ErrorStatus(100, it.message)
                })
                .autoDispose()
    }

    fun listFileByPath(path: String, page: Int, pageSize: Int, orderBy: Int, type: Int = -1) {
        repo.listFileByPath(path, page, pageSize, orderBy, type)
                .subscribe({
                    if (it.success) {
                        val fileList = mutableListOf<Direcotory>()
                        for (item in it.data!!.list) {
                            if(!item.locking) {
                                fileList.add(item)
                            }
                        }
                        listFileResult.value = fileList
                    } else {
                        errorStatus.value = ErrorStatus(it.status, it.message)
                    }
                }, {
                    errorStatus.value = ErrorStatus(100, it.message)
                })
                .autoDispose()
    }

    fun parseUrl(url: String) {
        repo.parseUrlS(url)
                .subscribe({
                    if (it.success) {
                        parseUrlResult.value = it.data
                    } else {
                        errorStatus.value = ErrorStatus(100, it.message)
                    }
                }, {
                    errorStatus.value = ErrorStatus(100, it.message)
                })
                .autoDispose()
    }

    fun offlineDownloadStart(taskHash: String, savePath: String, copyFile: Array<Int> = arrayOf()) {
        repo.offlineDownloadStart(taskHash, savePath, copyFile)
                .subscribe({

                }, {

                })
                .autoDispose()
    }

    fun removeFile(path: List<String>) {
        repo.removeFile(path)
                .subscribe({
                    if (it.success) {
                        removeResult.value = it.data
                    }
                }, {

                })
                .autoDispose()
    }
}
