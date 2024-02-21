
Const FPS=90
Type vector
	Field x#
	Field y#
	Field z#
End Type
Global CProd.vector=New vector
Type csys
   Field cx#,cy#,cz#
   Field mx#,my#,sps
End Type

Graphics3D 800,600,0,2
SetBuffer BackBuffer()

camera = CreateCamera()
PositionEntity camera,10,8,-5

Wireframe False

TOKSIM_CreateSimulator(0,-9.0,0)
TOKSIM_SetMaterial 0,0.0,0.6 

ColBank=CreateBank(104*10)
TOKSIM_SetCInfoBank ColBank
TOKSIM_SetCollisionResponse -1,1,3 ; Impulse & Feedback 
TOKSIM_SetCollisionResponse -1,2,0 ; No Impulse Or Feedback 


tex = LoadTexture("track.bmp")
Terrain = LoadMesh("track.3ds")
ScaleMesh Terrain,25.0,25.0,25.0 
RotateMesh Terrain,-90,0,0
EntityTexture terrain,tex
MakeTokCollider(Terrain)

SeedRnd MilliSecs()  

Global Body_Mesh = LoadMesh("truck_body.b3d")
ScaleMesh Body_Mesh,0.3,0.3,0.3
PositionMesh Body_Mesh,0.0,-2.1,0.0
RotateMesh Body_Mesh,0,180,0

Global Body_RB = TOKRB_Create()
TOKRB_AddBox Body_RB,2.0,1.0,3.0
TOKRB_SetPosition Body_RB,0,5,0
TOKRB_SetMass Body_RB,1.0
TOKRB_SetBoxInertiaTensor Body_RB,2.0,1.0,3.0,1.0
ExtraWeight = TOKRB_Create()
TOKRB_SetCollisionID ExtraWeight,2
TOKRB_SetMass ExtraWeight,0.4
TOKRB_SetSphereInertiaTensor ExtraWeight,0.5,0.4 
TOKRB_SetPosition ExtraWeight,0,5,1.5
ConnJoint = TOKJOINT_Create(2,ExtraWeight,Body_RB)
TOKJOINT_SetType ConnJoint,1 
TOKJOINT_SetPositionAndRotationWorld ConnJoint,0,5.0,1.5,0,0,0 
TOKJOINT_Enable ConnJoint,True

Global RR_Spr_RB = TOKRB_Create()
TOKRB_AddSphere RR_Spr_RB,1.0
TOKRB_SetPosition RR_Spr_RB,1.2,3.0,-1.50
TOKRB_SetMass RR_Spr_RB,1.0
TOKRB_SetSphereInertiaTensor RR_Spr_RB,1.0,1.0
TOKRB_SetCollisionID RR_Spr_RB,1 
RR_SprJoint = TOKJOINT_Create(2,RR_Spr_RB,Body_RB)
TOKJOINT_SetType RR_SprJoint,4 ; Slidejoint
TOKJOINT_SetPositionAndRotationWorld RR_SprJoint,1.2,3.0,-1.50,0,0,0 
TOKJOINT_Enable RR_SprJoint,True

Global RL_Spr_RB = TOKRB_Create()
TOKRB_AddSphere RL_Spr_RB,1.0
TOKRB_SetPosition RL_Spr_RB,-1.2,3.0,-1.5
TOKRB_SetMass RL_Spr_RB,1.0
TOKRB_SetSphereInertiaTensor RL_Spr_RB,1.0,1.0
TOKRB_SetCollisionID RL_Spr_RB,1 
RL_SprJoint = TOKJOINT_Create(2,RL_Spr_RB,Body_RB)
TOKJOINT_SetType RL_SprJoint,4 ; Slidejoint
TOKJOINT_SetPositionAndRotationWorld RL_SprJoint,-1.2,3.0,-1.5,0,0,0 
TOKJOINT_Enable RL_SprJoint,True

Global FR_Spr_RB = TOKRB_Create()
TOKRB_AddSphere FR_Spr_RB,1.0
TOKRB_SetPosition FR_Spr_RB,1.2,3.0,1.65
TOKRB_SetMass FR_Spr_RB,1.0
TOKRB_SetSphereInertiaTensor FR_Spr_RB,1.0,1.0
TOKRB_SetCollisionID FR_Spr_RB,1 
FR_SprJoint = TOKJOINT_Create(2,FR_Spr_RB,Body_RB)
TOKJOINT_SetType FR_SprJoint,4 ; Slidejoint
TOKJOINT_SetPositionAndRotationWorld FR_SprJoint,1.2,3.0,1.65,0,0,0 
TOKJOINT_Enable FR_SprJoint,True

Global FL_Spr_RB = TOKRB_Create()
TOKRB_AddSphere FL_Spr_RB,1.0
TOKRB_SetPosition FL_Spr_RB,-1.2,3.0,1.65
TOKRB_SetMass FL_Spr_RB,1.0
TOKRB_SetSphereInertiaTensor FL_Spr_RB,1.0,1.0
TOKRB_SetCollisionID FL_Spr_RB,1 
FL_SprJoint = TOKJOINT_Create(2,FL_Spr_RB,Body_RB)
TOKJOINT_SetType FL_SprJoint,4 ; Slidejoint
TOKJOINT_SetPositionAndRotationWorld FL_SprJoint,-1.2,3.0,1.65,0,0,0 
TOKJOINT_Enable FL_SprJoint,True


FR_Wheel=LoadMesh("truck_wheel.b3d")
ScaleMesh FR_Wheel,0.3,0.3,0.3
FL_Wheel=LoadMesh("truck_wheel.b3d")
ScaleMesh FL_Wheel,0.3,0.3,0.3
RR_Wheel=LoadMesh("truck_wheel.b3d")
ScaleMesh RR_Wheel,0.3,0.3,0.3
RL_Wheel=LoadMesh("truck_wheel.b3d")
ScaleMesh RL_Wheel,0.3,0.3,0.3

CamPoint = TOKRB_Create()
TOKRB_SetMass CamPoint,1.0
TOKRB_SetSphereInertiaTensor FR_Spr_RB,1.0,1.0
TOKRB_SetLinearDamping CamPoint,0.01

Centerpivot = CreatePivot()

CamTargetPivot=CreatePivot()

Global speed#=0

GripFactor#=0.2

light=CreateLight()
LightColor light,128,128,128 
PositionEntity light,255,255,-255

PointEntity light,Centerpivot

period=1000/FPS
time=MilliSecs()-period
Global timestep#=1.5/FPS
; MainLoop
While Not KeyHit(1)

	Repeat
		elapsed=MilliSecs()-time
	Until elapsed

	ticks=elapsed/period
	tween#=Float(elapsed Mod period)/Float(period)

	For k=1 To ticks
		time=time+period
		If k=ticks Then CaptureWorld

		TOKSIM_Advance(1.5/FPS,1)


	UpdateWorld

    PlaceMesh Body_Mesh,Body_RB
	PositionEntity FR_Wheel,TOKRB_GetX#(FR_Spr_RB),TOKRB_GetY#(FR_Spr_RB),TOKRB_GetZ#(FR_Spr_RB)
	RotateEntity FR_Wheel,EntityPitch#(Body_Mesh),EntityYaw#(Body_Mesh),EntityRoll#(Body_Mesh)
	TurnEntity FR_Wheel,0,Turn#,0 
	PositionEntity FL_Wheel,TOKRB_GetX#(FL_Spr_RB),TOKRB_GetY#(FL_Spr_RB),TOKRB_GetZ#(FL_Spr_RB)
	RotateEntity FL_Wheel,EntityPitch#(Body_Mesh),EntityYaw#(Body_Mesh),EntityRoll#(Body_Mesh)
	TurnEntity FL_Wheel,0,Turn#,0 
	PositionEntity RR_Wheel,TOKRB_GetX#(RR_Spr_RB),TOKRB_GetY#(RR_Spr_RB),TOKRB_GetZ#(RR_Spr_RB)
	RotateEntity RR_Wheel,EntityPitch#(Body_Mesh),EntityYaw#(Body_Mesh),EntityRoll#(Body_Mesh)
	PositionEntity RL_Wheel,TOKRB_GetX#(RL_Spr_RB),TOKRB_GetY#(RL_Spr_RB),TOKRB_GetZ#(RL_Spr_RB)
	RotateEntity RL_Wheel,EntityPitch#(Body_Mesh),EntityYaw#(Body_Mesh),EntityRoll#(Body_Mesh)

 	PointEntity camera,Body_Mesh

	;UpdateSpring 1.2,-0.5,-1.5,RR_Spr_RB,50,6.0,1.1
	;UpdateSpring -1.2,-0.5,-1.5,RL_Spr_RB,50,6.0,1.1
	;UpdateSpring 1.2,-0.5,1.65,FR_Spr_RB,50,6.0,1.1
	;UpdateSpring -1.2,-0.5,1.65,FL_Spr_RB,50,6.0,1.1
	UpdateSpring 1.2,-0.5,-1.5,RR_Spr_RB,50,15.0,1.1
	UpdateSpring -1.2,-0.5,-1.5,RL_Spr_RB,50,15.0,1.1
	UpdateSpring 1.2,-0.5,1.65,FR_Spr_RB,50,15.0,1.1
	UpdateSpring -1.2,-0.5,1.65,FL_Spr_RB,50,15.0,1.1 

	RRColl=False
	RLColl=False
	FRColl=False
	FLColl=False
	;Check Colliders
	If TOKSIM_GetCollisionCount()>0 Then
		 For c=0 To TOKSIM_GetCollisionCount()-1
			If PeekInt(ColBank,0+(c*104))=RR_Spr_RB Then RRColl=True
			If PeekInt(ColBank,0+(c*104))=RL_Spr_RB Then RLColl=True
			If PeekInt(ColBank,0+(c*104))=FR_Spr_RB Then FRColl=True
			If PeekInt(ColBank,0+(c*104))=FL_Spr_RB Then FLColl=True
	     Next
	EndIf

	;Driving-force

 If KeyDown(200) Then
  Acc#=Acc#+1.0
  If Acc#>11 Then Acc#=11
 ElseIf KeyDown(208) Then
  Acc#=Acc#-1.0
  If Acc#<6.0 Then Acc#=-6.0
 Else
  Acc#=Acc#*0.95
  If Acc#>-0.01 And Acc#<0.01 Then Acc#=0.0
 EndIf
	
	TFormVector 0,0,1,Body_Mesh,0
	vx#=TFormedX#()
	vy#=TFormedY#()
	vz#=TFormedZ#()
	
	 If RRColl=True Then
	  TOKRB_ApplyImpulse RR_Spr_RB,vx#*acc#*timestep#,vy#*acc#*timestep#,vz#*acc#*timestep#
     EndIf
	 If RLColl=True Then
	  TOKRB_ApplyImpulse RL_Spr_RB,vx#*acc#*timestep#,vy#*acc#*timestep#,vz#*acc#*timestep#
	 EndIf

	CarVelx#=TOKRB_GetVelocityX#(Body_RB)
	CarVely#=TOKRB_GetVelocityY#(Body_RB)
	CarVelz#=TOKRB_GetVelocityZ#(Body_RB)
	TFormNormal 0,0,1,Body_Mesh,0
	CarVel#=Abs(DotProduct#(TFormedX#(),TFormedY#(),TFormedZ#(),CarVelx#,CarVely#,CarVelz#))
	TurnFactor#=1.0-((1.0/35.0)*CarVel#)

MaxTurn#=27.0
CurrTurn#=MaxTurn#*TurnFactor#
If KeyDown(205) Then
 Turn#=Turn#-0.7
 If Turn#<-MaxTurn#*TurnFactor# Then Turn#=-MaxTurn#*TurnFactor#
ElseIf KeyDown(203) Then
 Turn#=Turn#+0.7
 If Turn#>MaxTurn#*TurnFactor# Then Turn#=MaxTurn#*TurnFactor#
Else
 Turn=Turn*0.95
 If Turn#>-0.1 And Turn#<0.1 Then Turn#=0.0
EndIf

	AngVelx#=TOKRB_GetAngularVelocityX#(Body_RB)
	AngVely#=TOKRB_GetAngularVelocityY#(Body_RB)
	AngVelz#=TOKRB_GetAngularVelocityZ#(Body_RB)
	TFormNormal 0,1,0,Body_Mesh,0
	AngVel#=Abs(DotProduct#(TFormedX#(),TFormedY#(),TFormedZ#(),AngVelx#,AngVely#,AngVelz#))

If KeyHit(57) Or (AngVel#>1.0 And CarVel#>12.0) Then
 Slip=True
EndIf

	If Slip=True Then 
	 GripFactor#=0.99
	EndIf	
	If AngVel#<0.7 And Slip=True Then 
	 GripFactor#=0.98
	 ;Slip=False
	EndIf	
	If AngVel#<0.5 And Slip=True Then 
	 GripFactor#=0.97
	 ;Slip=False
	EndIf	
	If AngVel#<0.3 And Slip=True Then 
	 GripFactor#=0.9
	 Slip=False
	EndIf	


If KeyHit(16) Then
 Slip=True
 TOKRB_ApplyTwist Body_RB,0,50,0
EndIf
	
	;Friction

	;Find sideslip-speed for RightRearWheel
    Vx#=TOKRB_GetVelocityX#(RR_Spr_RB)
    Vy#=TOKRB_GetVelocityY#(RR_Spr_RB)
    Vz#=TOKRB_GetVelocityZ#(RR_Spr_RB)
	TFormVector Vx#,Vy#,Vz#,0,Body_Mesh
	TFormVector TFormedX#()*GripFactor#,TFormedY#(),TFormedZ#(),Body_Mesh,0
	If RRColl=True Then
	 TOKRB_SetVelocity RR_Spr_RB,TFormedX#(),TFormedY#(),TFormedZ#()
 	EndIf

	;Find sideslip-speed for LeftRearWheel
    Vx#=TOKRB_GetVelocityX#(RL_Spr_RB)
    Vy#=TOKRB_GetVelocityY#(RL_Spr_RB)
    Vz#=TOKRB_GetVelocityZ#(RL_Spr_RB)
	TFormVector Vx#,Vy#,Vz#,0,Body_Mesh
	TFormVector TFormedX#()*GripFactor#,TFormedY#(),TFormedZ#(),Body_Mesh,0
	If RLColl=True Then
	 TOKRB_SetVelocity RL_Spr_RB,TFormedX#(),TFormedY#(),TFormedZ#()
	EndIf
	
	;Find sideslip-speed for RightFrontWheel
    Vx#=TOKRB_GetVelocityX#(FR_Spr_RB)
    Vy#=TOKRB_GetVelocityY#(FR_Spr_RB)
    Vz#=TOKRB_GetVelocityZ#(FR_Spr_RB)
	TFormVector Vx#,Vy#,Vz#,0,FR_Wheel
	TFormVector TFormedX#()*GripFactor#,TFormedY#(),TFormedZ#(),FR_Wheel,0
	If FRColl=True Then
	 TOKRB_SetVelocity FR_Spr_RB,TFormedX#(),TFormedY#(),TFormedZ#()
	EndIf
	
	;Find sideslip-speed for LeftFrontWheel
    Vx#=TOKRB_GetVelocityX#(FL_Spr_RB)
    Vy#=TOKRB_GetVelocityY#(FL_Spr_RB)
    Vz#=TOKRB_GetVelocityZ#(FL_Spr_RB)
	TFormVector Vx#,Vy#,Vz#,0,FL_Wheel
	TFormVector TFormedX#()*GripFactor#,TFormedY#(),TFormedZ#(),FL_Wheel,0
	If FLColl=True Then
	 TOKRB_SetVelocity FL_Spr_RB,TFormedX#(),TFormedY#(),TFormedZ#()
	EndIf


	;Drag
	TFormNormal 0,0,-1,Body_Mesh,0
	dragConstant# = 1.1
 	velX#=TOKRB_GetVelocityX#(Body_RB)
 	velY#=TOKRB_GetVelocityY#(Body_RB)
 	velZ#=TOKRB_GetVelocityZ#(Body_RB)

 	dot#=DotProduct#(TFormedX#(),TFormedY#(),TFormedZ#(),velX#,velY#,velZ#)
 	dragX#=dot#*TFormedX#()*-dragConstant#
 	dragY#=dot#*TFormedY#()*-dragConstant#
 	dragZ#=dot#*TFormedZ#()*-dragConstant#
	TOKRB_ApplyImpulse Body_RB,dragX#*timestep#,dragY#*timestep#,dragZ#*timestep#


	;Camera
	PositionEntity camtargetpivot,EntityX#(Body_Mesh),EntityY#(Body_Mesh),EntityZ#(Body_Mesh)
	RotateEntity camtargetpivot,EntityPitch#(Body_Mesh),EntityYaw#(Body_Mesh),EntityRoll#(Body_Mesh)
	MoveEntity camtargetpivot,0,3,-7
	UpdateCamera Camera,camtargetpivot,0.02


Next



	RenderWorld tween

	Flip False

Wend

TOKSIM_DestroySimulator()

End


Function UpdateSpring(px#,py#,pz#,Wheel_RB,Stiffness#,Damping#,SpringLength#)

;Add spring force
TFormPoint px#,py#,pz#,Body_Mesh,0
wpx#=TFormedX#()
wpy#=TFormedY#()
wpz#=TFormedZ#()

diffX#=wpx# - TOKRB_GetX#(Wheel_RB)
diffY#=wpy# - TOKRB_GetY#(Wheel_RB)
diffZ#=wpz# - TOKRB_GetZ#(Wheel_RB)
diffLen#=Sqr(diffX#*diffX#+diffY#*diffY#+diffZ#*diffZ#)
Displacement#=diffLen#-SpringLength#
nx#=diffx#/diffLen#
ny#=diffy#/diffLen#
nz#=diffz#/diffLen#
fx#=nx#*(Displacement#*Stiffness#)
fy#=ny#*(Displacement#*Stiffness#)
fz#=nz#*(Displacement#*Stiffness#)

;Add damping force
GetPointVel Body_RB,wpx#,wpy#,wpz#
Vx#=CProd\x#-TOKRB_GetVelocityX#(Wheel_RB)
Vy#=CProd\y#-TOKRB_GetVelocityY#(Wheel_RB)
Vz#=CProd\z#-TOKRB_GetVelocityZ#(Wheel_RB)

speed#=DotProduct#(nx#,ny#,nz#,Vx#,Vy#,Vz#)
dampX#=nx#*speed#*Damping#
dampY#=ny#*speed#*Damping#
dampZ#=nz#*speed#*Damping#

fx#=fx#+dampX#
fy#=fy#+dampY#
fz#=fz#+dampZ#

TOKRB_ApplyImpulse2 Body_RB,-fx#*timestep#,-fy#*timestep#,-fz#*timestep#,wpx#,wpy#,wpz#
TOKRB_ApplyImpulse Wheel_RB,fx#*timestep#,fy#*timestep#,fz#*timestep#

End Function

Function PlaceMesh(Mesh,RB)
   PositionEntity Mesh,TOKRB_GetX#(RB),TOKRB_GetY#(RB),TOKRB_GetZ#(RB) 
   RotateEntity Mesh,TOKRB_GetPitch#(RB),TOKRB_GetYaw#(RB),TOKRB_GetRoll#(RB),False
End Function


Function DotProduct#(x1#,y1#,z1#,x2#,y2#,z2#)
	DProd#=((x1#*x2#)+(y1#*y2#)+(z1#*z2#))
	Return DProd#
End Function
Function CrossProduct(x1#,y1#,z1#,x2#,y2#,z2#)
	CProd\x#=(y1#*z2#)-(z1#*y2#)
	CProd\y#=(z1#*x2#)-(x1#*z2#)
	CProd\z#=(x1#*y2#)-(y1#*x2#)
End Function

Function GetPointVel(RB,px#,py#,pz#)
 vx#=TOKRB_GetVelocityX#(RB)
 vy#=TOKRB_GetVelocityY#(RB)
 vz#=TOKRB_GetVelocityZ#(RB)
 avx#=TOKRB_GetAngularVelocityX#(RB)
 avy#=TOKRB_GetAngularVelocityY#(RB)
 avz#=TOKRB_GetAngularVelocityZ#(RB)
 CrossProduct avx#,avy#,avz#,px#-TOKRB_GetX#(RB),py#-TOKRB_GetY#(RB),pz#-TOKRB_GetZ#(RB)
 CProd\x#=CProd\x#+vx#
 CProd\y#=CProd\y#+vy#
 CProd\z#=CProd\z#+vz#
End Function

Function MakeTokCollider(mesh)
 scount=CountSurfaces(mesh)
 For ind=1 To scount
  surface=GetSurface(mesh,ind)
  ttltris=ttltris+CountTriangles(surface)
  ttlvert=ttlvert+CountVertices(surface)
 Next
 vertices=CreateBank(16*ttlvert)
 triangles=CreateBank(24*ttltris)
 offsetv=0
 offsett=0
 For ind=1 To scount
  surface = GetSurface(mesh,ind)
  ctr=CountTriangles(surface)
  tric=tric+cvt
  cvt=CountVertices(surface)
  ;fill bank with vertices
  For v=0 To cvt-1
   PokeFloat vertices,offsetv,VertexX#(surface,v)
   PokeFloat vertices,offsetv+4,VertexY#(surface,v)
   PokeFloat vertices,offsetv+8,VertexZ#(surface,v)
   PokeFloat vertices,offsetv+12,0.0
   offsetv=offsetv+16
  Next
  ;fill bank with triangles
  For v=0 To ctr-1
   PokeInt triangles,offsett,tric+TriangleVertex(surface,v,0)
   PokeInt triangles,offsett+4,tric+TriangleVertex(surface,v,1)
   PokeInt triangles,offsett+8,tric+TriangleVertex(surface,v,2)
   PokeInt triangles,offsett+12,2	; Material ID
   PokeInt triangles,offsett+16,0
   PokeInt triangles,offsett+20,0
   offsett=offsett+24
  Next
 Next

 ;Hand over the terrain data to Tokamak
 TOKSIM_SetStaticMesh vertices,ttlvert,triangles,ttltris
 ; Now we can free the banks as Tokamak has copied all data
 FreeBank vertices
 FreeBank triangles
End Function

Function RemoveComponent(x1#,y1#,z1#,x2#,y2#,z2#)
 tmpdot# = DotProduct#(x1#,y1#,z1#,x2#,y2#,z2#)
 CProd\x#=x1#-x2#*tmpdot#
 CProd\y#=y1#-y2#*tmpdot#
 CProd\z#=z1#-z2#*tmpdot#
End Function

Function GripFactor#(Vel#)
	If Vel#>=0.0 And Vel<3.0 Then
	 GripFactor#=0.50
	EndIf
	If Vel#>=3.0 And Vel<6.0 Then
	 GripFactor#=0.95
	EndIf
	If Vel#>=6.0 And Vel<10.0 Then
	 GripFactor#=0.97
	EndIf
	If Vel#>=10.0 And Vel<15.0 Then
	 GripFactor#=0.98
	EndIf
	If Vel#>=15.0 Then
	 GripFactor#=0.99
	EndIf
	Return 0.999
	Return GripFactor#
End Function


Function UpdateCamera(Camera,Target,Rate#)

CamX#=EntityX#(Camera)
CamY#=EntityY#(Camera)
CamZ#=EntityZ#(Camera)
DiffX#=EntityX#(Target)-CamX#
DiffY#=EntityY#(Target)-CamY#
DiffZ#=EntityZ#(Target)-CamZ#
TranslateEntity Camera,DiffX#*Rate#,DiffY#*Rate#,DiffZ#*Rate#

End Function