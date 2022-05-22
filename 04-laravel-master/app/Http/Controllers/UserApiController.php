<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use App\Modules\Users\Services\UserService;

class UserApiController extends Controller
{
    public function __construct(UserService $service)
    {
        $this->service = $service;
    }

    public function login(Request $request) {
        return $this->service->login($request);
    }

    public function register(Request $request) {
        return $this->service->register($request);
    }

    public function getUserById($userId) {
        return $this->service->getUserById($userId);
    }
}
