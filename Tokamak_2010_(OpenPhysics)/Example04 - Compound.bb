Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example04 / Compound"
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
			PositionEntity cam,0,5,-20
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

; Create Compound body
mass#=1
debug=1
obj.tok=	opBodyCreateCompound()

			;add a box
			w#=1:h#=1:d#=10	
			x#=0:y#=1:z#=0
			pitch#=0:yaw#=0:roll#=90			
obj\geom=	opBodyAddCompoundBox(obj\body,obj\mesh,w#,h#,d#,x#,y#,z#,pitch#,yaw#,roll#)
			;add a box
			w#=10:h#=1:d#=1	
			x#=0:y#=1:z#=0
			pitch#=0:yaw#=0:roll#=0			
obj\geom=	opBodyAddCompoundBox(obj\body,obj\mesh,w#,h#,d#,x#,y#,z#,pitch#,yaw#,roll#)

			;add a sphere
			radius#=1
			x#=0:y#=5:z#=0			
obj\geom=	opBodyAddCompoundSphere(obj\body,obj\mesh,radius#,x#,y#,z#)

			;add a capsule
			radius#=.5:height#=5
			x#=0:y#=0:z#=0
			pitch#=90:yaw#=0:roll#=0
obj\geom=	opBodyAddCompoundCapsule(obj\body,obj\mesh,radius#,height#,x#,y#,z#,pitch#,yaw#,roll#)
			;set body parameters
			opBodySetCompound(obj\body,obj\mesh,mass,debug)
			;opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
			opBodySetPos(obj\body,	0,10,0)
			opBodySetGfx(obj\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))				


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
	;opGeomSetEnt(temp_g,temp_m)
	; update physics & Position physic Box to Entity
	pms = MilliSecs()
	opWorldStep(0.025)
	Pyhsic_time = MilliSecs() - pms
	;temp

	
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