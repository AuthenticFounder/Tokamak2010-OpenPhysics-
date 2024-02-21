Global fpsindex#, fpstime#, fpsfold_millisecs#, fpsfps#, fpsfold2_Millisecs#
Function fps2#(time=1000)
	fpsindex=fpsindex+1
	fpstime=fpstime+MilliSecs()-fpsfold_millisecs
	If fpstime=>time
		fpsfps=fpsindex
		fpstime=0
		fpsindex=0
	EndIf
	fpsfold_Millisecs=MilliSecs()
	Return fpsfps
End Function
Function DrawFps(w=5,h=5)
Text w,h,fps()
End Function

Function fps(time=100)

	fpsindex=fpsindex+1
	fpstime=fpstime+MilliSecs()-fpsfold_millisecs
	
If fpstime>=time
		fpsfps=fpsindex*(1000.0/fpstime#)
		fpsindex=0
		fpstime=0
EndIf
	fpsfold_Millisecs=MilliSecs()
	Return fpsfps
End Function