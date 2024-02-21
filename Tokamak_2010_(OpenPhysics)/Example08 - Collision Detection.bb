Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example08 / Collision Detection."
AntiAlias True
SetBuffer BackBuffer()
SeedRnd MilliSecs()

center	=	CreatePivot()
cam		=	CreateCamera()
			CameraClsColor cam,255,255,255
			CameraFogColor cam,255,255,255
			CameraFogMode cam,1
			CameraFogRange cam,50,200
			CameraRange cam,.2,100000
			CreateListener Cam, .05
			PositionEntity cam,0,50,-80
			;PointEntity cam,center
			
			EntityRadius cam,1
			EntityType cam,COLL_CAM	
			opSetLogo(cam)	
						
			AmbientLight 50,42,39
light1	=	CreateLight()
			LightColor light1,228,224,187
			RotateEntity light1,20,50,0
light2	=	CreateLight()
			LightColor light2,88,126,147
			RotateEntity light2,-45,180+45,0			

;textures			
checktex1=	CreateCheckerTex()
			ScaleTexture checktex1,5,5

checktex2=	CreateCheckerTex()
			ScaleTexture checktex2,.5,.5
						
checktex=	CreateCheckerTex()
			ScaleTexture checktex,1,1

						
; Create mirror plane
plane	=	CreatePlane(16)
			opBodySetGfx(plane,checktex1)
			EntityAlpha plane,.8
			

			CreateMirror

; Create physic world
opWorldCreate(1)

;create some boxes
For i = 1 To 200
	box.tok	=	opBodyCreateBox(1,1,1,1)
				opBodySetPos(box\body,Rnd(-15,15),Rnd(24,50),Rnd(-15,15))
				opBodySetVel(box\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
				opBodySetRot(box\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
				opBodySetGfx(box\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))

	

Next

; Create test sphere, this body will detect collisions
test.tok=	opBodyCreatesphere(10,0)
			opBodySetPos(test\body,0,12,0)
			opBodySetGfx(test\mesh,checktex,255, 80,80)

;Load test sounds
Global snd_hit	=	Load3DSound("sound\Bounce.wav")


;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3

;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------

	;Move The camera
	FreeLook(cam,.1)
	
	;Apply Random Force
	If KeyHit(57)
		For p.tok = Each tok
			TOKRB_ApplyImpulse(p\body, 0, Rnd(10, 20), 0)
			TOKRB_ApplyTwist p\body, Rnd(-5, 5), Rnd(-5, 5), Rnd(-5, 5)
		Next
	EndIf
	
	; update physics & Position physic Box to Entity
	pms = MilliSecs()
	If MouseDown(2)
		opWorldStep(0.002)
	Else
		opWorldStep(0.025)
	End If
	Pyhsic_time = MilliSecs() - pms

	
	; Render world
	ms = MilliSecs()
	UpdateWorld
	RenderWorld
	Render_time = MilliSecs() - ms

	;rendercontacts show collisions and play a sound when collided	
	For p.tok = Each tok
		opRenderContacts(p\body,p\mesh,cam,0,255,0)
	Next
	
	Color 50,50,50
	Text 20,20,"FPS = "+Fps(50)
	Text 20,40,"Physics = "+Pyhsic_time
	Text 20,60,"Render = "+Render_time	
	Text 20,80,"bodys = "+opBodyCount		
	Text 40,100,"Collision: "+TOKSIM_GetCollisionCount%()

	
	Flip 1

Wend
opDestroyWorld()
End

Function opRenderContacts(body,mesh,cam,r%,g%,b%)	
	ncoll = TOKSIM_GetCollisionCount()
	For i=0 To ncoll-1
		
		If (opCollBodyA(colbank,i)=body And TOKRB_GetUserData(opCollBodyB(colbank,i))=0) Or (opCollBodyB(colbank,i)=body And TOKRB_GetUserData(opCollBodyB(colbank,i))=0) Then
			depth#=opGetCollisionDepth(colbank,i)
			;DebugLog depth#
			;here is the colision response part
			;apply sound			
			If Abs(depth)>3 Then
				c=EmitSound(snd_hit,mesh)
				ChannelVolume c, Abs(depth)  * 3
			EndIf
			
			;render contacts
			RenderContacts=1
			If RenderContacts=1
			Color r,g,b
			
			x# = opGetCollisionAX(colbank,i)
			y# = opGetCollisionAY(colbank,i)
			z# = opGetCollisionAZ(colbank,i)
			CameraProject cam,x,y,z
			sx# =ProjectedX():sy# =ProjectedY()
			Oval sx-2,sy-2,4,4
			x# = x# + opGetCollisionNX(colbank,i)
			y# = y# + opGetCollisionNY(colbank,i)
			z# = z# + opGetCollisionNZ(colbank,i)
			CameraProject cam,x,y,z
			sx2# =ProjectedX():sy2# =ProjectedY()
			Line sx,sy,sx2,sy2+(depth#*50)
			Text sx,sy,depth#
			Color 255,255,255
			End If
		EndIf

	Next

End Function