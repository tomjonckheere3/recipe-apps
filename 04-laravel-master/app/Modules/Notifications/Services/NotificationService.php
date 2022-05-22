<?php
namespace App\Modules\Notifications\Services;

use App\Models\User;
use App\Notifications\FavouritedRecipe;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Notification;
use App\Models\Notification as NotificationModel;
use Carbon\Carbon;
use Exception;

class NotificationService {

    protected $rules = [
        "endpoint" => "required",
        "keys.auth" => "required",
        "keys.p256dh" => "required",
    ];

    public function __construct(User $userModel, NotificationModel $notificationModel)
    {

        $this->userModel = $userModel;
        $this->notificationModel = $notificationModel;
    }

    public function registerUserForNotifications($userId, Request $request) {
        $this->errors = null;
        $validator = Validator::make($request->all(), $this->rules);

        if ($validator->fails()) {
            $this->errors = $validator->errors();
            Log::info($this->errors);
            return response()->json($validator->errors()->toJson(), 400);
        }

        $endpoint = $request->endpoint;
        $token = $request->keys["auth"];
        $key = $request->keys["p256dh"];
        $user = $this->userModel->find($userId);
        $user->updatePushSubscription($endpoint, $key, $token);

        return response()->json(["success" => true], 200);
    }

    public function sendFavouritedNotification($username, $recipe) {
        $body = $username . " favourited your recipe: " . $recipe->recipeName;
        try {
            Notification::send(User::find($recipe->userId), new FavouritedRecipe($body));
            $this->addNotification($recipe->userId, $body);
        } catch (Exception $e) {
            Log::info($e);
        }
        Log::info("message");

    }

    public function addNotification($userId, $body) {
        $currentTime = Carbon::now()->toDateTimeString();
        $this->notificationModel::create(["userId" => $userId, "notificationContent" => $body, "date" => $currentTime]);
    }

    public function getNotificationsOfUser($userId) {
        return $this->notificationModel::where("userId", $userId)->orderBy("date", "DESC")->get();
    }

}
