<?php

namespace App\Http\Controllers;

use App\Modules\Notifications\Services\NotificationService;
use Illuminate\Http\Request;

class NotificationApiController extends Controller
{
    public function __construct(NotificationService $service)
    {
        $this->service = $service;
    }

    public function registerUserForNotifications($userId, Request $request) {
        return $this->service->registerUserForNotifications($userId, $request);
    }

    public function getNotificationsOfUser($userId) {
        return $this->service->getNotificationsOfUser($userId);
    }
}
