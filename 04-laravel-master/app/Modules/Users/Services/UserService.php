<?php
namespace App\Modules\Users\Services;

use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class UserService {

    protected $rules = [
        "username" => "required|string",
        "password" => "required|string"
    ];

    public function __construct(User $userModel)
    {
        $this->userModel = $userModel;
    }

    public function login(Request $request) {
        $req = Validator::make($request->all(), $this->rules);

        if ($req->fails()) {
            return response()->json($req->errors(), 422);
        }

        if (! $token = auth()->attempt($req->validated())) {
            return response()->json(["Auth error" => "Unauthorized"], 401);
        }

        $userId = $this->userModel::select("userId")->where("username", "=", $request->username)
                                        ->first()
                                        ->userId;

        return $this->generateToken($token, $userId);
    }

    public function register(Request $request) {
        $req = Validator::make($request->all(), $this->rules);

        if ($req->fails()) {
            return response()->json($req->errors()->toJson(), 400);
        }

        try {
            $this->userModel::create(array_merge(
                $req->validated(),
                ['password' => bcrypt($request->password)]));

        } catch (\Exception $e) {
            return response()->json([
                "error" => "This user already exists"
            ], 400);
        }

        $userId = $this->userModel::select("userId")->where("username", "=", $request->username)
                                        ->first()
                                        ->userId;

        $token = auth()->attempt($req->validated());

        return response()->json([
            "userId" => $userId,
            "jwt" => $token
        ], 201);
    }

    public function getUserById($userId) {
        return $this->userModel->find($userId);
    }

    protected function generateToken($token, $userId) {
        return response()->json([
            "userId" => $userId,
            "jwt" => $token
        ]);
    }

}
