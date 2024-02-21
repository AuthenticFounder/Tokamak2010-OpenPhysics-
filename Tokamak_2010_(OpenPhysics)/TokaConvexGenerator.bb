;for fullscreen use
;user32.dll to return back to the main blitz window
;Const SW_HIDE             			= 0
;Const SW_SHOW             			= 5
;Const Title$ = "Exec test"
;AppTitle(Title$)
;hWnd = api_FindWindow(0,Title$)
;If hWnd = 0 Then RuntimeError("Blitz window needs a title!")

;11/06/2004 : Fixed a memory release bug in the dll source.
;-------------------------------------------------------------------------
;qhull1.dll is a modified and simplified version of qhull1.0
;By Tsiantas Elias 20 feb 2004.
;Full modified source is provided.
;-------------------------------------------------------------------------
;YOU NEED THIS DECLS FILE

;.lib "qhull1.dll"
;qhull%(infile$,outfile$,delta#,pf%,fo%):"qh@20"
;-------------------------------------------------------------------------
;
;Qhull1.0 is free software and hence this modification falls under
;the same license as qhull1.0.
;It may be obtained via anonymous ftp from geom.umn.edu
;Read the Copying_qhull.rtf for more.
;
;
;                     Copyright (c) 1993
;       The National Science And Technology Research Center For
;	     Computation And Visualization of Geometric Structures
;
;-------------------------------------------------------------------------

;location of the program atjency
ProgramDir$="Bin\"


;*************************************************************************
;Edited (Deformed) by Dylan McCall, May 2005 for APOE Convex Hull Function

Dim txv(3)

Type TRIS
	Field x0#,y0#,z0#
	Field x1#,y1#,z1#
	Field x2#,y2#,z2#
End Type
;*************************************************************************
;*************************************************************************
;The main function that calls the qhull dll
;It returns a welded convex hull of the model you send it.

Dim qvix#(0),qviy#(0),qviz#(0)
Dim dat(0)

Function create_Tok_Hull(entity,file3$,delta#,weld=1)

Local file$="temp_"+file3$+".txt"
Local file2$="temp_"+file3$+".off"
Local points%
Local vx#,vy#,vz#
Local k%,l%
Local count%,res%
Local v0%,v1%,v2%
Local vt0%,vt1%,vt2%
Local t_f$
Local rx#,ry#,rz#
Local px#,py#,pz#
Local su,hull


rx=EntityPitch(entity,1)
ry=EntityYaw(entity,1)
rz=EntityRoll(entity,1)
RotateEntity entity,0,0,0,1
px=EntityX(entity,1)
py=EntityY(entity,1)
pz=EntityZ(entity,1)
PositionEntity entity,0,0,0,1


Local surfs=CountSurfaces(entity)
If surfs=0 Return 0


For k=1 To surfs
	surf=GetSurface(entity,k)
	points=points+CountVertices(surf)
Next

Dim qvix#(points),qviy#(points),qviz#(points)

;output for adjency
Local fil=WriteFile(file$)
WriteLine fil,"3";! dimension 3d
WriteLine fil,points


count=0

For k=1 To surfs
	surf=GetSurface(entity,k)
	
	For l=0 To CountVertices(surf)-1
		vx=VertexX(surf,l)
		vy=VertexY(surf,l)
		vz=VertexZ(surf,l)
		TFormPoint vx,vy,vz,entity,0
		WriteLine fil,TFormedX()+" "+TFormedY()+" "+TFormedZ()
		qvix(count)=TFormedX()
		qviy(count)=TFormedY()
		qviz(count)=TFormedZ()
		count=count+1
	Next

Next


CloseFile fil


;set it back to it's original rotation and position
RotateEntity entity,rx,ry,rz,1
PositionEntity entity,px,py,pz,1

;make sure the file is written before we call qhull1.dll
While FileType(file$)<>1
Delay 30
Wend



;Executing external file in order to prevent loss of data if the generator crashes
;---------------------------------
;force qhull1.dll to print out only
;face indices and force output
;hWnd = SystemProperty("AppHWND")
;Exec$=Chr$(34)+apppath$+"CHull_CrashShield.exe"+Chr$(34)+hWnd+" <> "+file$ + " > " + file2$ + " < " + delta
;ExecFile Exec$
res=qhull(file$,file2$,delta,1,1)


For loop = 0 To 500
    Delay 50
    If FileType(file2$)=1 Then Exit
Next
If FileType(file2$)=0
    Dim qvix(0),qviy(0),qviz(0)
    ;Delete the Input file
    While FileType(file$)=1
        Delay 30
        DeleteFile file$
    Wend
    Return 0
EndIf


fil=ReadFile(file2$)

t_f$=ReadLine(fil)
If Lower(Left$(t_f$,1))<>"n"
    Dim qvix(0),qviy(0),qviz(0)
    ;Delete the Input file
    While FileType(file$)=1
        Delay 30
        DeleteFile file$
    Wend
    ;Delete the output file
    While FileType(file2$)=1
        Delay 30
        DeleteFile file2$
    Wend
    Return 0
EndIf

;create the hull mesh
hull=CreateMesh()
su=CreateSurface(hull)

While Not Eof(fil)
t_f$=ReadLine(fil)
	If t_f$<>""
		v0=get_string(t_f$,1)
		v1=get_string(t_f$,2)
		v2=get_string(t_f$,3)
			vt0=AddVertex ( su,qvix(v0),qviy(v0),qviz(v0) )
			vt1=AddVertex ( su,qvix(v1),qviy(v1),qviz(v1) )
			vt2=AddVertex ( su,qvix(v2),qviy(v2),qviz(v2) )
		
			AddTriangle (su,vt0,vt2,vt1)
	EndIf
Wend

CloseFile fil

;Adjacency input (replace QHull mesh generator input)
;Delete the original input file
While FileType(file$)=1
Delay 30
DeleteFile file$
Wend

While FileType(file$)=1
Delay 30
Wend

rx=EntityPitch(entity,1)
ry=EntityYaw(entity,1)
rz=EntityRoll(entity,1)
RotateEntity entity,0,0,0,1
px=EntityX(entity,1)
py=EntityY(entity,1)
pz=EntityZ(entity,1)
PositionEntity entity,0,0,0,1


surfs=CountSurfaces(entity)
If surfs=0 Return 0

points = 0


For k=1 To surfs
	surf=GetSurface(entity,k)
	points=points+CountVertices(surf)
Next

fil=WriteFile(file$)
WriteLine fil,points + " " + Chr(10)

count=0

For k=1 To surfs
	surf=GetSurface(entity,k)

	For l=0 To CountVertices(surf)-1
		vx=VertexX(surf,l)
		vy=VertexY(surf,l)
		vz=VertexZ(surf,l)
		TFormPoint vx,vy,vz,entity,0
		If Not l = CountVertices(surf)-1 Then space$ = "  " + Chr(10) Else space$ = ""
		WriteLine fil,TFormedX()+" "+TFormedY()+" "+TFormedZ() + space$
		count=count+1
	Next

Next


CloseFile fil

;set it back to it's original rotation and position
RotateEntity entity,rx,ry,rz,1
PositionEntity entity,px,py,pz,1

;make sure the file is written before we run Adjacency
While FileType(file$)<>1
Delay 30
Wend

Exec$=Chr$(34)+"bin\adjacency.exe"+Chr$(34)+" "+file$+" "+file3$
DebugLog exec$
ExecFile Exec$ 

;make sure the file is written Before we continue
While FileType(file3$)<>1
Delay 30
Wend

;Delete the Input file
While FileType(file$)=1
Delay 30
DeleteFile file$
Wend


;Delete the output file
While FileType(file2$)=1
Delay 30
DeleteFile file2$
Wend


Dim qvix(0),qviy(0),qviz(0)

;weld the hull
If weld weld3(hull)

UpdateNormals hull
Return hull


End Function



;*************************************************************************
;function needed to parse the output from qhull1.dll
Function get_string$(in$, num)
stemp$ = ""
numtemp = 0
For temp=1 To Len(in$)
If Mid(in$, temp, 1)<>" "
stemp$ = stemp$ + Mid(in$, temp, 1)
Else
numtemp = numtemp + 1
If numtemp = num
Return stemp$
Else
stemp$ = ""
EndIf
EndIf
Next
Return ""
End Function

Function RemoveBeforeColon$(file$) 

	If Len(file$)>0 
	
		For i=Len(file$) To 1 Step -1 
		
			mi$=Mid$(file$,i,1) 
			If mi$=":" Then Return name$ Else name$=mi$+name$ 
		
		Next 
	
	EndIf 
	
	Return name$ 

End Function


;*************************************************************************
;
;Cutdown version of the weld routine from the code archives

Function Weld3(mesh,thresh#=0.001)
	Dim txv(3)

		Local su=GetSurface(mesh,1)

		For tq = 0 To CountTriangles(su)-1
			txv(0) = TriangleVertex(su,tq,0)
			txv(1) = TriangleVertex(su,tq,1)
			txv(2) = TriangleVertex(su,tq,2)
			vq.TRIS = New TRIS
			vq\x0# = VertexX(su,txv(0))
			vq\y0# = VertexY(su,txv(0))
			vq\z0# = VertexZ(su,txv(0))
			vq\x1# = VertexX(su,txv(1))
			vq\y1# = VertexY(su,txv(1))
			vq\z1# = VertexZ(su,txv(1))
			vq\x2# = VertexX(su,txv(2))
			vq\y2# = VertexY(su,txv(2))
			vq\z2# = VertexZ(su,txv(2))
		Next
		
		ClearSurface su
		
		For vq.tris = Each tris
		
				vt1=findvert3(su,vq\x0#,vq\y0#,vq\z0#,thresh#)
				
				If vt1=-1 Then

					vt1=AddVertex(su,vq\x0#,vq\y0#,vq\z0#)
					vt1 = mycount
					mycount = mycount +1
				EndIf
		
				vt2=findvert3(su,vq\x1#,vq\y1#,vq\z1#,thresh#)
				
				If Vt2=-1 Then

					vt2=AddVertex( su,vq\x1#,vq\y1#,vq\z1#)
					vt2 = mycount
					mycount = mycount +1
				EndIf
				
				vt3=findvert3(su,vq\x2#,vq\y2#,vq\z2#,thresh#)
				
				If vt3=-1 Then

					vt3=AddVertex(su,vq\x2#,vq\y2#,vq\z2#)
					vt3 = mycount
					mycount = mycount +1
				EndIf
		
			AddTriangle su,vt1,vt2,vt3
		
		Next
		
		Delete Each tris
		mycount=0

UpdateNormals(mesh)

End Function

	
Function findvert3(su,x2#,y2#,z2#,thresh#);
	
	For t=0 To CountVertices(su)-1;
		If Abs(VertexX(su,t)-x2#)<thresh# Then 
			If Abs(VertexY(su,t)-y2#)<thresh# Then 
				If Abs(VertexZ(su,t)-z2#)<thresh# Then 
							Return t
						EndIf
					EndIf
				EndIf
	Next
	Return -1
End Function




;*************************************************************************