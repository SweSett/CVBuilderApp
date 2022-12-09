package edu.miu.CVBuilderApp.workdialogs

import edu.miu.CVBuilderApp.data.Work

interface WorkDialogCommunicator {
    fun onAddWOrk(work: Work)
}