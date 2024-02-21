Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics Example12 / Water"
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
			CreateListener Cam, .1
			PositionEntity cam,-30,30,-30
			PointEntity cam,center
			
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
Global checktex1,checktex2,checktex
		
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

; Create cube pyramid
size=10
Spacing = 0
Cubesize#=1
pos_y# = 1
Offset = -size * (Cubesize*2 + Spacing) * 0.5
While size
		For  i=0 To size-1

				Pos_x = Offset + i * (CubeSize*2 + Spacing)					
				box.tok	=	opbodyCreateBox(CubeSize*2,CubeSize*2,CubeSize*2,1)					
   				opBodySetPos(box\body,pos_x, pos_y,0)
				opBodySetGfx(box\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))					
				opBodySetAutoSleep(box\body,10)
				;opBodySetAutoSleepTreshold(box\body,10)

		Next
	Offset = Offset +Cubesize;
	pos_y =  pos_y +(Cubesize*2 + Spacing)
	size=size-1
Wend 

;create a waterplane
water = CreatePlane()
		PositionEntity water,0,10,0
		EntityAlpha water,.5
		EntityColor water,80,90,180


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
		
	;set water plane
	opSetWater(10,.02,.009,.008)

	;create new primitive
	primit = Rand(1,50)
	If primit <5
		dx# = Rnd(1,3):dy# = Rnd(1,3):dz# = Rnd(1,3);size
		mass# = Rnd(1,5)
		
		;Create corresponded primitive
		Select primit
			Case 1;cube
				obj.tok	=	opBodyCreateBox(dx,dy,dz,mass)
							opBodySetPos(obj\body,Rnd(-20,20),Rnd(43,47),Rnd(-20,20))
							opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
							opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
							opBodySetGfx(obj\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))


			Case 2;sphere
				obj.tok	=	opBodyCreateSphere(dx,mass)
							opBodySetPos(obj\body,Rnd(-20,20),Rnd(43,47),Rnd(-20,20))
							opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
							opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
							opBodySetGfx(obj\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))

				
			Case 3;Capsule
				obj.tok	=	opBodyCreateCapsule(dx,dz,mass)
							opBodySetPos(obj\body,Rnd(-20,20),Rnd(43,47),Rnd(-20,20))
							opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
							opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
							opBodySetGfx(obj\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))

		End Select

	EndIf
	
	; update physics & Position physic Box to Entity	
	pms = MilliSecs()	
	opWorldStep(0.025)
	Pyhsic_time = MilliSecs() - pms	
	
	; Render world
	ms = MilliSecs()
	UpdateWorld
	RenderWorld
	Render_time = MilliSecs() - ms

	Color 50,50,50
	Text 20,20,"FPS = "+Fps(50)
	Text 20,40,"Physics = "+Pyhsic_time
	Text 20,60,"Render = "+Render_time
	Text 20,80,"bodys = "+opBodyCount
	
	Flip 1

Wend
opDestroyWorld()
End