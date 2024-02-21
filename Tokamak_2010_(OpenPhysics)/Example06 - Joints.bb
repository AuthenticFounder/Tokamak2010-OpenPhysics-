Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example06 / Joints"
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
			PositionEntity cam,0,10,-20
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

; create ball joint
Ball_X=-15
Ball_Y=15
Ball_Z=0

Box1_1.tok=	opBodyCreateSphere(1,0);mass 0 = static body
			opBodySetPos(Box1_1\body,Ball_X,Ball_Y,Ball_Z)
			opBodySetGfx(Box1_1\mesh,checktex,255, 150, 150)
			
							
Box1.tok=	opBodyCreateCapsule(1,1,10)
			opBodySetPos(Box1\body,Ball_X,Ball_Y-5,Ball_Z)
			opBodySetrot(Box1\body,90,0,0)
			opBodySetGfx(Box1\mesh,checktex,150, 220, 150)



BallJoint	=	opJointBallCreate(Ball_X,Ball_Y,Ball_Z,0,0,0, Box1\body, Box1_1\body);joint is attatched to a body


; create hinge joint
Hinge_X=-7
Hinge_Y=20
Hinge_Z=0
hinge_test=	CreateCube():ScaleMesh hinge_test,6,.1,.1
			PositionEntity hinge_test,Hinge_X,Hinge_Y,Hinge_Z
			opBodySetGfx(hinge_test,checktex2,255,150, 150)
Box2_1.tok=	opBodyCreateBox(6,.2,.2,0);mass 0 = static body
			opBodySetPos(Box2_1\body,Hinge_X,Hinge_Y,Hinge_Z)
			opBodySetGfx(Box2_1\mesh,checktex2,255,150, 150)
						
Box2.tok=	opBodyCreateBox(5,.1,10,1)
			opBodySetPos(Box2\body,Hinge_X,Hinge_Y,Hinge_Z-5)
			opBodySetGfx(Box2\mesh,checktex,150, 220, 150)
						
HingeJoint=	opJointHingeCreate(Hinge_X,Hinge_Y,Hinge_Z, 0,0,90,Box2_1\body, Box2\body);joint is attatched to space
			opJointHingeSetLimit(HingeJoint,-160,-40)
; create slider joint
Slider_X=0
Slider_Y=20
Slider_Z=0

temp	=	CreateCube():ScaleMesh temp,.1,6,.1:PositionMesh temp,0,13,0

Box3_1.tok=	opBodyCreateBox(2,2,2,0);mass 0 = static body
			opBodySetPos(Box3_1\body,Slider_X,Slider_Y,Slider_Z)
			opBodySetGfx(Box3_1\mesh,checktex2,255,150, 150)
						
Box3.tok=	opBodyCreateBox(2,2,2,1)
			opBodySetPos(Box3\body,Slider_X,Slider_Y,Slider_Z)
			opBodySetGfx(Box3\mesh,checktex,150, 220, 150)

SliderJoint=opJointSliderCreate(Slider_X,Slider_Y,Slider_Z,0,0,0, Box3\body,Box3_1\body);joint is at attatched to space
			opJointSliderSetLimit(SliderJoint,-6*2,0)
				
; create hinge2 joint
Hinge2_X=7
Hinge2_Y=20
Hinge2_Z=0
hinge2_test	=	CreateCube():ScaleMesh hinge2_test,6,.1,.1
			PositionEntity hinge2_test,Hinge2_X,Hinge2_Y,Hinge2_Z
			opBodySetGfx(hinge2_test,checktex2,255,150, 150)
						
Box4_1.tok=	opBodyCreateBox(5,1,1,0)
			opBodySetPos(Box4_1\body,Hinge2_X,Hinge2_Y,Hinge2_Z)
			opBodySetGfx(Box4_1\mesh,checktex,255,150, 150)

Box4.tok=	opBodyCreateBox(5,10,1,1)
			opBodySetPos(Box4\body,Hinge2_X,Hinge2_Y-5,Hinge2_Z)
			opBodySetGfx(Box4\mesh,checktex,150, 220, 150)
			
Hinge2Joint=opJointHingeCreate(Hinge2_X,Hinge2_Y,Hinge2_Z, 0,0,90, Box4\body,Box4_1\body);joint is attatched to another body, this cant be atatched to space
			;ODE_dJointAddHinge2Torques(joint%, torque1#, torque2#)
			
; create universal joint /ball socket
Univ_X=15
Univ_Y=15
Univ_Z=0
			
Box5_1.tok=	opBodyCreateSphere(1,0);mass 0 = static body
			opBodySetPos(Box5_1\body,Univ_X,Univ_Y,Univ_Z)
			opBodySetGfx(Box5_1\mesh,checktex,255, 150, 150)
						
Box5.tok=	opBodyCreateCapsule(1,2,1)
			opBodySetPos(Box5\body,Univ_X,Univ_Y-5,Univ_Z)
			opBodySetrot(Box5\body,0,0,0)
			opBodySetGfx(Box5\mesh,checktex,150, 220, 150)
			
cyl1	=	CreateCube()
			ScaleMesh cyl1,1,.1,.1
			PositionMesh cyl1,1,3,0	
				
cyl2	=	CreateCube()
			ScaleMesh cyl2,.1,2,.1
			PositionMesh cyl2,0,2,0

			AddMesh cyl1,Box5\mesh:FreeEntity cyl1
			AddMesh cyl2,Box5\mesh:FreeEntity cyl2
								
UnivJoint=	opJointBallCreate(Univ_X,Univ_Y,Univ_Z, 0,0,0, Box5_1\body, Box5\body);joint is atatched to space
			opJointBallSetLimit(UnivJoint,1,-90,90,1,-90,90)


;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3

;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------
	t = t+1
	rad#=.5
	spd#=3
	
	; move ball
	opBodySetPos(Box1_1\body,Ball_X - rad*Cos(t*spd),Ball_Y,Ball_Z+rad*Sin(t*spd))	
	
	; move hinge2
	opBodySetPos(Box4_1\body,Hinge2_X - rad*Cos(t*spd),Hinge2_Y,Hinge2_Z+rad*Sin(t*spd))
	
	; move Universal
	opBodySetPos(Box5_1\body,Univ_X - rad*Cos(t*spd),Univ_Y,Univ_Z+rad*Sin(t*spd))
	; add slider force
	;R=Rnd(1,100)
	;If R=1 Then ODE_dJointAddSliderForce(SliderJoint,50)
	
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
	If MouseDown(2)
		opWorldStep(0.003)
	Else
		opWorldStep(0.025)
	End If
	Pyhsic_time = MilliSecs() - pms
	;temp
	;opGeomSetEnt(geom,mesh)
	
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
	
	;joint info	
	Text 20,90,"BallJoint "
	Text 40,100,"Pos_X = "+TOKJOINT_GetFrameAX(BallJoint)
	Text 40,110,"Pos_Y = "+TOKJOINT_GetFrameAY(BallJoint)
	Text 40,120,"Pos_Z = "+TOKJOINT_GetFrameAZ(BallJoint)

	Text 20+150,20,"HingeJoint "
	Text 40+150,30,"Pos_X = "+TOKJOINT_GetFrameBX(HingeJoint)
	Text 40+150,40,"Pos_Y = "+TOKJOINT_GetFrameBY(HingeJoint)
	Text 40+150,50,"Pos_Z = "+TOKJOINT_GetFrameBZ(HingeJoint)
	;Text 40+150,60,"AngleRate = "+GetHingeAngleRate(HingeJoint)
	
	Text 20+280,20,"SliderJoint "
	Text 40+280,30,"Pos = "+TOKJOINT_GetFrameAY(SliderJoint)
	
	Text 20+460,20,"HingeJoint "
	Text 40+460,100,"Angle1 = "+TOKJOINT_GetFrameAPitch(Hinge2Joint)

	Text 20+600,20,"UniversalJoint "


	
	Flip 1

Wend
opDestroyWorld()
End