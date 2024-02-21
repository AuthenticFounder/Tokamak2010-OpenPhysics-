.lib "tokamakwrapper.dll"

; *** Simulator ***
; If the values below isn't set before you create the simulator,
; default values will be used.  
TOKSIM_SetRigidBodiesCount(Count%):"_TOKSIM_SetRigidBodiesCount@4"
;Default = 50

TOKSIM_SetAnimatedBodiesCount(Count%):"_TOKSIM_SetAnimatedBodiesCount@4"
;Default = 50

TOKSIM_SetRigidParticleCount(Count%):"_TOKSIM_SetRigidParticleCount@4"
;Default = 50

TOKSIM_SetControllersCount(Count%):"_TOKSIM_SetControllersCount@4"
;Default = 50

TOKSIM_SetOverlappedPairsCount(Count%):"_TOKSIM_SetOverlappedPairsCount@4"
;Default = 1225 = (TBodies*(TBodies-1))/2 where TBodies is the total number
;of Rigid & Animated bodies

TOKSIM_SetGeometriesCount(Count%):"_TOKSIM_SetGeometriesCount@4"
;Default = 50

TOKSIM_SetConstraintsCount(Count%):"_TOKSIM_SetConstraintsCount@4"
;Default = 100

TOKSIM_SetConstraintSetsCount(Count%):"_TOKSIM_SetConstraintSetsCount@4"
;Default = 100

TOKSIM_SetConstraintBufferSize(Count%):"_TOKSIM_SetConstraintBufferSize@4"
;Default = 2000

TOKSIM_SetStaticMeshNodesStartCount(Count%):"_TOKSIM_SetStaticMeshNodesStartCount@4"
;Default = 200

TOKSIM_SetStaticMeshNodesGrowByCount(Count%):"_TOKSIM_SetStaticMeshNodesGrowByCount@4"
;Default = -1 = The nodecount will increase by 100% when growing.

TOKSIM_CreateSimulator%(GravityX#,GravityY#,GravityZ#):"_TOKSIM_CreateSimulator@12"
TOKSIM_DestroySimulator():"_TOKSIM_DestroySimulator@0"
TOKSIM_Advance(timestep#,SubSteps%):"_TOKSIM_Advance@8"
TOKSIM_Advance2(timestep#,minTimeStep#,maxTimeStep#):"_TOKSIM_Advance@12"
TOKSIM_GetPhysicsTime#():"_TOKSIM_GetPhysicsTime@0"
TOKSIM_SetStaticMesh(Vertices*,VertexCount%,Triangles*,TriangleCount%):"_TOKSIM_SetStaticMesh@16"
TOKSIM_FreeStaticMesh():"_TOKSIM_FreeStaticMesh@0"

TOKSIM_SetMaterial#(Index%,Friction#,Restitution#):"_TOKSIM_SetMaterial@12"
TOKSIM_GetMaterialFriction#(Index%):"_TOKSIM_GetMaterialFriction@4"
TOKSIM_GetMaterialRestitution#(Index%):"_TOKSIM_GetMaterialRestitution@4"

TOKSIM_SetCInfoBank(CInfoBank*):"_TOKSIM_SetCInfoBank@4"
TOKSIM_SetCollisionResponse(CollisionID1%,CollisionID2%,Response%):"_TOKSIM_SetCollisionResponse@12"
TOKSIM_GetCollisionCount%():"_TOKSIM_GetCollisionCount@0"

TOKSIM_GetGravityX#():"_TOKSIM_GravityX@0"
TOKSIM_GetGravityY#():"_TOKSIM_GravityY@0"
TOKSIM_GetGravityZ#():"_TOKSIM_GravityZ@0"
TOKSIM_SetGravity(GravityX#,GravityY#,GravityZ#):"_TOKSIM_SetGravity@12"

; *** Rigid Body *** 
TOKRB_Create%():"_TOKRB_Create@0"
TOKRB_CreateParticle%():"_TOKRB_CreateParticle@0"
TOKRB_Free(RigidBody%):"_TOKRB_Free@4"
TOKRB_SetCollisionID(RigidBody%,ColID%):"_TOKRB_SetCollisionID@8"
TOKRB_GetCollisionID%(RigidBody%):"_TOKRB_GetCollisionID@4"

TOKRB_GetX#(RigidBody%):"_TOKRB_GetX@4"
TOKRB_GetY#(RigidBody%):"_TOKRB_GetY@4"
TOKRB_GetZ#(RigidBody%):"_TOKRB_GetZ@4"
TOKRB_SetPosition(RigidBody%,X#,Y#,Z#):"_TOKRB_SetPosition@16"

TOKRB_GetPitch#(RigidBody%):"_TOKRB_GetPitch@4"
TOKRB_GetYaw#(RigidBody%):"_TOKRB_GetYaw@4"
TOKRB_GetRoll#(RigidBody%):"_TOKRB_GetRoll@4"
TOKRB_SetRotation(RigidBody%,Pitch#,Yaw#,Roll#):"_TOKRB_SetRotation@16"

TOKRB_AddBox%(RigidBody%,Width#,Height#,Depth#):"_TOKRB_AddBox@16"
TOKRB_AddSphere%(RigidBody%,Diameter#):"_TOKRB_AddSphere@8"
TOKRB_AddCylinder%(RigidBody%,Diameter#,Height#):"_TOKRB_AddCylinder@12"
TOKRB_AddConvex%(RigidBody%,Bank*,BankSize%):"_TOKRB_AddConvex@12"
TOKRB_UpdateBoundingInfo(RigidBody%):"_TOKRB_UpdateBoundingInfo@4"
TOKRB_SetBoxInertiaTensor(RigidBody%,Width#,Height#,Depth#,Mass#):"_TOKRB_SetBoxInertiaTensor@20"
TOKRB_SetSphereInertiaTensor(RigidBody%,Diameter#,Mass#):"_TOKRB_SetSphereInertiaTensor@12"
TOKRB_SetCylinderInertiaTensor(RigidBody%,Diameter#,Height#,Mass#):"_TOKRB_SetCylinderInertiaTensor@16"
TOKRB_SetMass(RigidBody%,Mass#):"_TOKRB_SetMass@8"

TOKRB_SetLinearDamping(RigidBody%,Damping#):"_TOKRB_SetLinearDamping@8"
TOKRB_SetAngularDamping(RigidBody%,Damping#):"_TOKRB_SetAngularDamping@8"

TOKRB_IsIdle%(RigidBody%):"_TOKRB_IsIdle@4"
TOKRB_CollideConnected(RigidBody%,Connected%):"_TOKRB_CollideConnected@8"
;TOKRB_CollideDirectlyConnected(RigidBody%,Connected%):"_TOKRB_CollideDirectlyConnected@8"
TOKRB_SetSleepingParameter(RigidBody%,sleepparam#):"_TOKRB_SetSleepingParameter@8"
TOKRB_Active(RigidBody%,Active%):"_TOKRB_Active@8"
TOKRB_IsActive%(RigidBody%):"_TOKRB_IsActive@4"
TOKRB_GravityEnable(RigidBody%,Enable%):"_TOKRB_GravityEnable@8"
TOKRB_IsGravityEnabled%(RigidBody%):"_TOKRB_IsGravityEnabled@4"

TOKRB_ApplyImpulse(RigidBody%,X#,Y#,Z#):"_TOKRB_ApplyImpulse@16"
TOKRB_ApplyImpulse2(RigidBody%,X#,Y#,Z#,XPos#,YPos#,ZPos#):"_TOKRB_ApplyImpulse2@28"
TOKRB_ApplyTwist(RigidBody%,X#,Y#,Z#):"_TOKRB_ApplyTwist@16"

TOKRB_SetForce(RigidBody%,X#,Y#,Z#):"_TOKRB_SetForce@16"
TOKRB_SetForce2(RigidBody%,X#,Y#,Z#,XPos#,YPos#,ZPos#):"_TOKRB_SetForce2@28"
TOKRB_SetTorque(RigidBody%,X#,Y#,Z#):"_TOKRB_SetTorque@16"

TOKRB_GetVelocityX#(RigidBody%):"_TOKRB_GetVelocityX@4"
TOKRB_GetVelocityY#(RigidBody%):"_TOKRB_GetVelocityY@4"
TOKRB_GetVelocityZ#(RigidBody%):"_TOKRB_GetVelocityZ@4"
TOKRB_SetVelocity(RigidBody%,X#,Y#,Z#):"_TOKRB_SetVelocity@16"

TOKRB_GetAngularMomentumX#(RigidBody%):"_TOKRB_GetAngularMomentumX@4"
TOKRB_GetAngularMomentumY#(RigidBody%):"_TOKRB_GetAngularMomentumY@4"
TOKRB_GetAngularMomentumZ#(RigidBody%):"_TOKRB_GetAngularMomentumZ@4"
TOKRB_SetAngularMomentum(RigidBody%,X#,Y#,Z#):"_TOKRB_SetAngularMomentum@16"

TOKRB_GetAngularVelocityX#(RigidBody%):"_TOKRB_GetAngularVelocityX@4"
TOKRB_GetAngularVelocityY#(RigidBody%):"_TOKRB_GetAngularVelocityY@4"
TOKRB_GetAngularVelocityZ#(RigidBody%):"_TOKRB_GetAngularVelocityZ@4"

TOKRB_GetVelocityAtPointX#(RigidBody%,X#,Y#,Z#):"_TOKRB_GetVelocityAtPointX@16"
TOKRB_GetVelocityAtPointY#(RigidBody%,X#,Y#,Z#):"_TOKRB_GetVelocityAtPointY@16"
TOKRB_GetVelocityAtPointZ#(RigidBody%,X#,Y#,Z#):"_TOKRB_GetVelocityAtPointZ@16"

TOKRB_RemoveGeometry(RigidBody%,Geometry%):"_TOKRB_RemoveGeometry@8"
TOKRB_GetGeometryCount%(RigidBody%):"_TOKRB_GetGeometryCount@4"
TOKRB_BeginIterateGeometry(RigidBody%):"_TOKRB_BeginIterateGeometry@4"
TOKRB_GetNextGeometry%(RigidBody%):"_TOKRB_GetNextGeometry@4"

TOKRB_AddSensor%(RigidBody%,PosX#,PosY#,PosZ#,DirX#,DirY#,DirZ#):"_TOKRB_AddSensor@28"
TOKRB_BeginIterateSensor(RigidBody%):"_TOKRB_BeginIterateSensor@4"
TOKRB_GetNextSensor%(RigidBody%):"_TOKRB_GetNextSensor@4"
TOKRB_RemoveSensor(RigidBody%,Sensor%):"_TOKRB_RemoveSensor@8"
TOKRB_SetUserData(RigidBody%,UserData%):"_TOKRB_SetUserData@8"
TOKRB_GetUserData%(RigidBody%):"_TOKRB_GetUserData@4"

; *** Animated Body ***
TOKAB_Create%():"_TOKAB_Create@0"
TOKAB_Free(AnimatedBody%):"_TOKAB_Free@4"
TOKAB_SetCollisionID(AnimatedBody%,ColID%):"_TOKAB_SetCollisionID@8"
TOKAB_GetCollisionID%(AnimatedBody%):"_TOKAB_GetCollisionID@4"

TOKAB_GetX#(AnimatedBody%):"_TOKAB_GetX@4"
TOKAB_GetY#(AnimatedBody%):"_TOKAB_GetY@4"
TOKAB_GetZ#(AnimatedBody%):"_TOKAB_GetZ@4"
TOKAB_SetPosition(AnimatedBody%,X#,Y#,Z#):"_TOKAB_SetPosition@16"

TOKAB_GetPitch#(AnimatedBody%):"_TOKAB_GetPitch@4"
TOKAB_GetYaw#(AnimatedBody%):"_TOKAB_GetYaw@4"
TOKAB_GetRoll#(AnimatedBody%):"_TOKAB_GetRoll@4"
TOKAB_SetRotation(AnimatedBody%,Pitch#,Yaw#,Roll#):"_TOKAB_SetRotation@16"

TOKAB_AddBox%(AnimatedBody%,Width#,Height#,Depth#):"_TOKAB_AddBox@16"
TOKAB_AddSphere%(AnimatedBody%,Diameter#):"_TOKAB_AddSphere@8"
TOKAB_AddCylinder%(AnimatedBody%,Diameter#,Height#):"_TOKAB_AddCylinder@12"
TOKAB_AddConvex%(AnimatedBody%,Bank*,BankSize%):"_TOKAB_AddConvex@12"

TOKAB_UpdateBoundingInfo(AnimatedBody%):"_TOKAB_UpdateBoundingInfo@4"

TOKAB_RemoveGeometry(AnimatedBody%,Geometry%):"_TOKAB_RemoveGeometry@8"
TOKAB_GetGeometryCount%(AnimatedBody%):"_TOKAB_GetGeometryCount@4"
TOKAB_BeginIterateGeometry(AnimatedBody%):"_TOKAB_BeginIterateGeometry@4"
TOKAB_GetNextGeometry%(AnimatedBody%):"_TOKAB_GetNextGeometry@4"

TOKAB_CollideConnected(AnimatedBody%,Connected%):"_TOKAB_CollideConnected@8"
;TOKAB_CollideDirectlyConnected(AnimatedBody%,Connected%):"_TOKAB_CollideDirectlyConnected@8"
TOKAB_SetUserData(AnimatedBody%,UserData%):"_TOKAB_SetUserData@8"
TOKAB_GetUserData%(AnimatedBody%):"_TOKAB_GetUserData@4"

; *** Geometry ***
TOKGEOM_SetPositionAndRotation(Geometry%,X#,Y#,Z#,Pitch#,Yaw#,Roll#):"_TOKGEOM_SetPositionAndRotation@28"
TOKGEOM_SetMaterialIndex(Geometry%,Index%):"_TOKGEOM_SetMaterialIndex@8"
TOKGEOM_GetMaterialIndex%(Geometry%):"_TOKGEOM_GetMaterialIndex@4"
TOKGEOM_SetUserData(Geometry%,UserData%):"_TOKGEOM_SetUserData@8"
TOKGEOM_GetUserData%(Geometry%):"_TOKGEOM_GetUserData@4"

; *** Joint ***
TOKJOINT_Create%(ConnectionType%,BodyA%,BodyB%):"_TOKJOINT_Create@12"
TOKJOINT_Free%(Joint%):"_TOKJOINT_Free@4"
TOKJOINT_SetType(Joint%,JointType%):"_TOKJOINT_SetType@8"
TOKJOINT_SetPositionAndRotationWorld(Joint%,X#,Y#,Z#,Pitch#,Yaw#,Roll#):"_TOKJOINT_SetPositionWorld@28"
TOKJOINT_SetPositionAndRotationFrameA(Joint%,X#,Y#,Z#,Pitch#,Yaw#,Roll#):"_TOKJOINT_SetPositionFrameA@28"
TOKJOINT_SetPositionAndRotationFrameB(Joint%,X#,Y#,Z#,Pitch#,Yaw#,Roll#):"_TOKJOINT_SetPositionFrameB@28"
TOKJOINT_SetJointLength(Joint%,Length#):"_TOKJOINT_SetJointLength@8"
TOKJOINT_GetJointLength#(Joint%):"_TOKJOINT_GetJointLength@4"
TOKJOINT_Enable(Joint%,Enable):"_TOKJOINT_Enable@8"
TOKJOINT_IsEnabled%(Joint%):"_TOKJOINT_IsEnabled@4"
TOKJOINT_SetUpperLimit(Joint%,Limit#):"_TOKJOINT_SetUpperLimit@8"
TOKJOINT_SetLowerLimit(Joint%,Limit#):"_TOKJOINT_SetLowerLimit@8"
TOKJOINT_SetUpperLimit2(Joint%,Limit#):"_TOKJOINT_SetUpperLimit2@8"
TOKJOINT_SetLowerLimit2(Joint%,Limit#):"_TOKJOINT_SetLowerLimit2@8"
TOKJOINT_GetUpperLimit#(Joint%):"_TOKJOINT_GetUpperLimit@4"
TOKJOINT_GetLowerLimit#(Joint%):"_TOKJOINT_GetLowerLimit@4"
TOKJOINT_GetUpperLimit2#(Joint%):"_TOKJOINT_GetUpperLimit2@4"
TOKJOINT_GetLowerLimit2#(Joint%):"_TOKJOINT_GetLowerLimit2@4"
TOKJOINT_EnableLimit(Joint%,Enabled%):"_TOKJOINT_EnableLimit@8"
TOKJOINT_EnableLimit2(Joint%,Enabled%):"_TOKJOINT_EnableLimit2@8"
TOKJOINT_IsLimitEnabled%(Joint%):"_TOKJOINT_IsLimitEnabled@4"
TOKJOINT_IsLimit2Enabled%(Joint%):"_TOKJOINT_IsLimit2Enabled@4"
TOKJOINT_SetEpsilon(Joint%,Epsilon#):"_TOKJOINT_SetEpsilon@8"
TOKJOINT_SetIterations(Joint%,Iterations%):"_TOKJOINT_SetIterations@8"
TOKJOINT_GetEpsilon#(Joint%):"_TOKJOINT_GetEpsilon@4"
TOKJOINT_GetIterations%(Joint%):"_TOKJOINT_GetIterations@4"
TOKJOINT_GetFrameAX#(Joint%):"_TOKJOINT_GetFrameAX@4"
TOKJOINT_GetFrameAY#(Joint%):"_TOKJOINT_GetFrameAY@4"
TOKJOINT_GetFrameAZ#(Joint%):"_TOKJOINT_GetFrameAZ@4"
TOKJOINT_GetFrameBX#(Joint%):"_TOKJOINT_GetFrameBX@4"
TOKJOINT_GetFrameBY#(Joint%):"_TOKJOINT_GetFrameBY@4"
TOKJOINT_GetFrameBZ#(Joint%):"_TOKJOINT_GetFrameBZ@4"
TOKJOINT_GetFrameAPitch#(Joint%):"_TOKJOINT_GetFrameAPitch@4"
TOKJOINT_GetFrameAYaw#(Joint%):"_TOKJOINT_GetFrameAYaw@4"
TOKJOINT_GetFrameARoll#(Joint%):"_TOKJOINT_GetFrameARoll@4"
TOKJOINT_GetFrameBPitch#(Joint%):"_TOKJOINT_GetFrameBPitch@4"
TOKJOINT_GetFrameBYaw#(Joint%):"_TOKJOINT_GetFrameBYaw@4"
TOKJOINT_GetFrameBRoll#(Joint%):"_TOKJOINT_GetFrameBRoll@4"
TOKJOINT_SetDampingFactor(Joint%,Damping#):"_TOKJOINT_SetDampingFactor@8"
TOKJOINT_GetDampingFactor#(Joint%):"_TOKJOINT_GetDampingFactor@4"
TOKJOINT_EnableMotor(Joint%,Enabled%):"_TOKJOINT_EnableMotor@8"
TOKJOINT_IsMotorEnabled%(Joint%):"_TOKJOINT_IsMotorEnabled@4"
TOKJOINT_SetMotorSpeed(Joint%,DesiredForce#,MaxForce#):"_TOKJOINT_SetMotorSpeed@12"
TOKJOINT_EnableMotor2(Joint%,Enabled%):"_TOKJOINT_EnableMotor2@8"
TOKJOINT_IsMotor2Enabled%(Joint%):"_TOKJOINT_IsMotor2Enabled@4"
TOKJOINT_SetMotor2Speed(Joint%,DesiredForce#,MaxForce#):"_TOKJOINT_SetMotor2Speed@12"

; *** Sensor ***
TOKSENSOR_SetLineSensor(Sensor%,PosX#,PosY#,PosZ#,DirX#,DirY#,DirZ#):"_TOKSENSOR_SetLineSensor@28"
TOKSENSOR_GetLineVectorX#(Sensor%):"_TOKSENSOR_GetLineVectorX@4"
TOKSENSOR_GetLineVectorY#(Sensor%):"_TOKSENSOR_GetLineVectorY@4"
TOKSENSOR_GetLineVectorZ#(Sensor%):"_TOKSENSOR_GetLineVectorZ@4"
TOKSENSOR_GetLineUnitVectorX#(Sensor%):"_TOKSENSOR_GetLineUnitVectorX@4"
TOKSENSOR_GetLineUnitVectorY#(Sensor%):"_TOKSENSOR_GetLineUnitVectorY@4"
TOKSENSOR_GetLineUnitVectorZ#(Sensor%):"_TOKSENSOR_GetLineUnitVectorZ@4"
TOKSENSOR_GetLinePosX#(Sensor%):"_TOKSENSOR_GetLinePosX@4"
TOKSENSOR_GetLinePosY#(Sensor%):"_TOKSENSOR_GetLinePosY@4"
TOKSENSOR_GetLinePosZ#(Sensor%):"_TOKSENSOR_GetLinePosZ@4"
TOKSENSOR_GetDetectDepth#(Sensor%):"_TOKSENSOR_GetDetectDepth@4"
TOKSENSOR_GetDetectNormalX#(Sensor%):"_TOKSENSOR_GetDetectNormalX@4"
TOKSENSOR_GetDetectNormalY#(Sensor%):"_TOKSENSOR_GetDetectNormalY@4"
TOKSENSOR_GetDetectNormalZ#(Sensor%):"_TOKSENSOR_GetDetectNormalZ@4"
TOKSENSOR_GetDetectContactPointX#(Sensor%):"_TOKSENSOR_GetDetectContactPointX@4"
TOKSENSOR_GetDetectContactPointY#(Sensor%):"_TOKSENSOR_GetDetectContactPointY@4"
TOKSENSOR_GetDetectContactPointZ#(Sensor%):"_TOKSENSOR_GetDetectContactPointZ@4"
TOKSENSOR_GetDetectRigidBody%(Sensor%):"_TOKSENSOR_GetDetectRigidBody@4"
TOKSENSOR_GetDetectAnimatedBody%(Sensor%):"_TOKSENSOR_GetDetectAnimatedBody@4"
TOKSENSOR_GetDetectMaterial%(Sensor%):"_TOKSENSOR_GetDetectMaterial@4"
TOKSENSOR_SetUserData(Sensor%,UserData%):"_TOKSENSOR_SetUserData@8"
TOKSENSOR_GetUserData%(Sensor%):"_TOKSENSOR_GetUserData@4"





